package com.smart.controllers;

import java.util.Random;

import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ForgotPasswordController {
	
	Random random=new Random(1000);
	
	@RequestMapping("/forgot")
	public String forgotpassword(Model model)
	{
		model.addAttribute("title", "forgotpassword-Smart Contact Manager");
		
		return "forgotPassword_form";
	}
	
	@RequestMapping("/confirm_otp")
	public String Send_OTP(Model model)
	{
		model.addAttribute("title", "Confirm OTP-Smart Contact Manager");
		
		int otp=random.nextInt(99999);
		
		return "otp_form";
	}

}
