package com.wipro.iaf.task.TaskManagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wipro.iaf.task.TaskManagement.entity.MappingRequest;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.enums.Status;
import com.wipro.iaf.task.TaskManagement.services.AdminService;
import com.wipro.iaf.task.TaskManagement.services.MappingRequestService;
import com.wipro.iaf.task.TaskManagement.services.TaskService;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private TaskService taskService;
    @Autowired private AdminService adminService;
    @Autowired private MappingRequestService mappingService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model, HttpSession session,Authentication auth) {
    	
    	
    	    User user = userService.findByEmail(auth.getName());
    	    session.setAttribute("currentUser", user);

        List<Task> tasks = taskService.getAllTasks();

        List<User> users = userService.getAllUsers().stream()
            .filter(item -> !item.getRole().equals(Role.ADMIN))
            .collect(Collectors.toList());

        List<User> pendingManagers = userService.getUsersPendingManagerApproval();
        List<MappingRequest> mappingRequests = mappingService.getPendingRequests();

        // 6 status values matching your enum
        List<String> chartLabels = List.of("To Do", "In Progress", "In Review", "Done", "On Hold", "Not Started");
        List<Long> chartValues = Arrays.asList(
            taskService.countByStatus(Status.TODO),
            taskService.countByStatus(Status.IN_PROGRESS),
            taskService.countByStatus(Status.IN_REVIEW),
            taskService.countByStatus(Status.DONE),
            taskService.countByStatus(Status.ON_HOLD),
            taskService.countByStatus(Status.NOT_STARTED)
        );

        // Add all model attributes
        model.addAttribute("users", users);
        model.addAttribute("tasks", tasks);
        model.addAttribute("pendingManagers", pendingManagers);
        model.addAttribute("mappingRequests", mappingRequests);

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartValues", chartValues);

        model.addAttribute("taskTitles", tasks.stream().map(Task::getTitle).toList());
        model.addAttribute("estimatedHours", tasks.stream().map(Task::getEstimatedHours).toList());
        model.addAttribute("actualHours", tasks.stream().map(Task::getActualHours).toList());

        return "dashboard/administrator";
    }



    @PostMapping("/approve-role")
    public String approveManager(@RequestParam Long userId) {
        adminService.approveManagerRole(userId);
        return "redirect:/admin/dashboard#requests";
    }

    @PostMapping("/reject-role")
    public String rejectManager(@RequestParam Long userId) {
        adminService.rejectManagerRole(userId);
        return "redirect:/admin/dashboard#requests";
    }

    @PostMapping("/delete-task")
    public String deleteTask(@RequestParam Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/admin/dashboard#tasks";
    }
    
    @PostMapping("/users/toggle/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        User user = userService.getUserById(id).get();
        user.setActive(!user.isActive());
        userService.save(user);
        return "redirect:/admin/dashboard#users";
    }
    
 

    @PostMapping("/mapping-requests/approve")
    public String approveMapping(@RequestParam Long requestId) {
        mappingService.approveRequest(requestId);
        return "redirect:/admin/dashboard#mapping";
    }
    
    @PostMapping("/mapping-requests/reject")
    public String rejectMappingRequest(@RequestParam Long requestId) {
        mappingService.deleteById(requestId);
        return "redirect:/admin/dashboard#mapping";
    }
    
    @PostMapping("/change-password")
    public String changeUserPassword(@RequestParam Long userId,
                                     @RequestParam String newPassword,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserPassword(userId, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating password: " + e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }




}

