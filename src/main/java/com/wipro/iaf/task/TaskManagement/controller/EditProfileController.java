package com.wipro.iaf.task.TaskManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Controller
	public class EditProfileController {

	    @Autowired
	    private UserService userService;

	    @GetMapping("/profile/edit")
	    public String showEditForm(Model model, Authentication auth) {
	        String email = auth.getName();
	        User user = userService.findByEmail(email);
	        model.addAttribute("user", user);
	        return "profile/edit";
	    }

	    @PostMapping("/profile/edit")
	    public String updateProfile(@ModelAttribute("user") User formUser, Authentication auth, Model model) {
	        String updated = userService.updateUserProfile(formUser, auth.getName());
	        model.addAttribute("message", updated);
	        model.addAttribute("user", userService.findByEmail(auth.getName()));
	        model.addAttribute("pending", userService.findByEmail(auth.getName()).isPendingManagerApproval());
	        return "profile/edit";
	    }
	}

