package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	
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
	public String processContact(@ModelAttribute Contact contact, Principal principal)
	{
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		
		System.out.println("DATA" + contact);
		
		System.out.println("Added to dataase");
		return "normal/add_contact_form";
	}
}
