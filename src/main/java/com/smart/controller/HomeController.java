package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
//	@Autowired
//	private UserRepository userRepository;
//	
//	@GetMapping("/test")
//	@ResponseBody
//	public String test()
//	{
//		User user = new User();
//		user.setName("Vivek Kulhar");
//		user.setEmail("vivek@kulhar.com");
//		
//		userRepository.save(user);
//		return "Working";
//	}
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model m)
	{
		
		m.addAttribute("title", "Home - Smart Contact Manager");
		
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
		
		m.addAttribute("title", "About - Smart Contact Manager");
		
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m)
	{
		
		m.addAttribute("title", "Register - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}
	
	//handler for registering user
	@RequestMapping(value="/do_register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user")User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false") boolean agreement, Model model, HttpSession session)
	{
		
		try {
			if(!agreement)
			{
				System.out.println("You have not agreed the terms and conditions");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("ERROR"+result1.toString());
				model.addAttribute("yser",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			
			System.out.println("Agreement " + agreement);
			System.out.println("USER " +user);
			
			User result = this.userRepository.save(user);
			
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			return "signup";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something Went Wrong !!"+e.getMessage(),"alert-danger"));
			
			return "signup";
		}
		
		
	}
}
