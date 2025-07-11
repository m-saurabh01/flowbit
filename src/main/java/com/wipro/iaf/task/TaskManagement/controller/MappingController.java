package com.wipro.iaf.task.TaskManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.services.MappingRequestService;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Controller
@RequestMapping("/mapping")
public class MappingController {

    @Autowired private MappingRequestService mappingService;
    @Autowired private UserService userService;

    @GetMapping("/request")
    public String showRequestForm(Model model,Authentication auth) {
    	User user =userService.findByEmail(auth.getName());
    	model.addAttribute("currentManager", user.getManager());
        model.addAttribute("managers", userService.getAllCustomManagers(user));
        return "mapping/request";
    }

    @PostMapping("/request")
    public String submitRequest(@RequestParam Long managerId, Authentication auth, Model model) {
    	User user =userService.findByEmail(auth.getName());
        String msg = mappingService.createMappingRequest(auth.getName(), managerId);
        model.addAttribute("message", msg);
        model.addAttribute("currentManager", user.getManager());
        model.addAttribute("managers", userService.getAllCustomManagers(user));
        return "mapping/request";
    }
}

