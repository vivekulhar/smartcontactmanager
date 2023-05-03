package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	//this method will run everytime
	//method for adding common data to response
	@ModelAttribute 
	public void addCommonData(Model model, Principal principal)
	{
		String userName = principal.getName();
		
		System.out.println("USERNAME"+userName);
		
		
		//get the user using username(Email)
		User user = userRepository.getUserByUserName(userName);
		
		System.out.println("USER" + user);
		
		model.addAttribute("user", user);
	}
	
	//dashboard-home
	@RequestMapping("/index")
	public String index(Model model, Principal principal)
	{
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
		
	}
	
	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContacForm(Model model)
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	//Processing add contact form
	@PostMapping("/process-contact")
	public String processContact(
			@ModelAttribute Contact contact, 
			@RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session)
	{
		
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			// processing and uploading file..
			
			if(file.isEmpty())
			{
				//if the file is empty then try our message
				System.out.println("File is Empty");
				contact.setImage("contact.png");
				
			}else {
				//upload the file to folder and update the name to contact
				
				contact.setImage(file.getOriginalFilename());
				
				File saveFile = new ClassPathResource("static/image").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("Image is Uploaded");
				
				
			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println("DATA" + contact);
			
			System.out.println("Added to database");
			
			
			//message success.......
			session.setAttribute("message", new Message("Your contact is added!! Add more..","success"));
			
			
		} catch (Exception e) {
			
			System.out.println("ERROR" + e.getMessage());
			
			e.printStackTrace();
			
			//Error message
			
			session.setAttribute("message", new Message("Something went wrong!! Try again..","danger"));
			
		}
		
		return "normal/add_contact_form";
	}
	
	//show contacts handler
	// per page = 5[n]
	// current page= 0[page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model m, Principal principal)
	{
		m.addAttribute("title", "Show User Contacts");
		
		//contact ki list ko bhejna hai
		String userName = principal.getName();
		
//		
		User user = this.userRepository.getUserByUserName(userName);
//		
//		List<Contact> contacts=user.getContacts();
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		
		m.addAttribute("totalPages",contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//showing particular contact details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal)
	{
		System.out.println("CID"+cId);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		
		Contact contact = contactOptional.get();
		
		//
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==contact.getUser().getId())
		{
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "normal/contact_detail";
	}
	
}
