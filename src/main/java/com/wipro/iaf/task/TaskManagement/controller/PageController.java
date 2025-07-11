package com.wipro.iaf.task.TaskManagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/signup")
	public String signupPage() {
		return "signup";
	}
	
	@GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
	
}
