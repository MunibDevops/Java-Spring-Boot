package com.smart.controllers;

//import java.security.Principal;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.smart.Dao.UserRepository;
//import com.smart.Entities.Contact;
import com.smart.Entities.User;
import com.smart.helper.Message;

@Controller

public class HomeController {
	  
	  @Autowired
		BCryptPasswordEncoder passwordEncoder;
		@Autowired
		UserRepository repository;
	    
	  @RequestMapping("/home")  
	  public String home(Model model)
	  {
		  model.addAttribute("title","Home - Smart Contact Manager");
	      return "home";
	  }
	  @RequestMapping("/about")  
	  public String about(Model model)
	  {
		  model.addAttribute("title","About - Smart Contact Manager");
	      return "about";
	  }
	  @RequestMapping("/signup")  
	  public String signup(Model model)
	  {
		  
		  model.addAttribute("title","Signup - Smart Contact Manager");
	      model.addAttribute("user",new User());
		  return "signup";
	  }
	  
	  @RequestMapping(value="do_register",method=RequestMethod.POST)
	  public String registerUser(Model model,@ModelAttribute("user")User user,@RequestParam(value="agreement",defaultValue="false")boolean agreement,HttpSession session)
	  {
		  try {
			  
			  if(!agreement)
			  {
				  System.out.println("You Donot Agree Terms and Condition");
				  throw new Exception("You Donot Agree Terms and Condition");
			  }
			  user.setRole("ROLE_USER");
			  user.setEnabled(true);
			  user.setImageurl("default.png");
			  user.setPassword(passwordEncoder.encode(user.getPassword()));
			  
			  User result=this.repository.save(user);
			  
			  model.addAttribute("user",new User());
			  session.setAttribute("message",new Message("Successfully registered !! ","alert-success"));
			  return "signup";
			  
			  
		  }catch(Exception e)
		  {
			  e.printStackTrace();
			  model.addAttribute("user", user);
			  session.setAttribute("message",new Message("Something Went Wrong !! "+e.getMessage(),"alert-danger"));
			  return "signup";
		  }
		  
		 
	  }
	  @RequestMapping("/signin")
	  public String login(Model model)
	  {
		  model.addAttribute("title","Login - Smart Content Manager");
		  return"login";
	  }
	  

	
	
}
