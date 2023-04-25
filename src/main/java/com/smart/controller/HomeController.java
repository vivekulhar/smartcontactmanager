package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;

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
	
	@RequestMapping("/signup/")
	public String signup(Model m)
	{
		
		m.addAttribute("title", "Register - Smart Contact Manager");
		
		return "signup";
	}
}
