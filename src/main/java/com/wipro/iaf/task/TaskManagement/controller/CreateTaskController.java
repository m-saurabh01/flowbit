package com.wipro.iaf.task.TaskManagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wipro.iaf.task.TaskManagement.dto.TaskDto;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.entity.Attachment;
import com.wipro.iaf.task.TaskManagement.enums.Priority;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.enums.Status;
import com.wipro.iaf.task.TaskManagement.services.AttachmentService;
import com.wipro.iaf.task.TaskManagement.services.TaskService;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Controller
public class CreateTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private AttachmentService attachmentService;

    @PreAuthorize("hasAnyRole('MANAGER')")
    @GetMapping("/tasks/create")
    public String showCreateForm(Model model, Authentication auth) {
        User currentUser = userService.findByEmail(auth.getName());
        
        if (currentUser.getRole() == Role.MANAGER) {
        	List<User> userList=userService.getEmployeesUnderManager(currentUser.getEmail());
            model.addAttribute("users",userList);
        } else if (currentUser.getRole() == Role.ADMIN) {
            model.addAttribute("users", userService.getAllUsers());
        }
        model.addAttribute("taskDto", new TaskDto());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("statuses", List.of(Status.NOT_STARTED));
        return "tasks/create";
    }


    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute TaskDto taskDto,
                             @RequestParam("attachments") MultipartFile[] files,
                             Authentication auth) {
    	User currentUser = userService.findByEmail(auth.getName());
        Task task = new Task();
        task.setTitle(taskDto.getTitle());

        String sanitizedDescription = Jsoup.clean(taskDto.getDescription(), Safelist.basicWithImages());
        task.setDescription(sanitizedDescription);

        List<User> assignedUsers = taskDto.getAssignedUserIds().stream()
            .map(id -> userService.getUserById(id).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        task.setAssignedUsers(assignedUsers);

        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());
        task.setTags(taskDto.getTags());
        task.setEstimatedHours(taskDto.getEstimatedHours() != null ? taskDto.getEstimatedHours() : 0);

        if (taskDto.getDeadline() != null && !taskDto.getDeadline().isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                task.setDeadline(LocalDate.parse(taskDto.getDeadline(), formatter));
            } catch (Exception e) {
                System.out.println("Invalid date format: " + taskDto.getDeadline());
            }
        }

        task.setCreatedDate(LocalDateTime.now());
        task.setUpdatedDate(LocalDateTime.now());
        task.setCreatedBy(currentUser.getId());

        taskService.createTask(task);

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                if (!fileName.matches(".*\\.(pdf|docx|png|jpg|jpeg)$")) {
                    System.out.println("Rejected file: " + fileName + " due to extension.");
                    continue;
                }
                if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
                    System.out.println("Rejected file: " + fileName + " due to size.");
                    continue;
                }

                try {
                    byte[] fileData = file.getBytes();

                    Attachment attachment = new Attachment();
                    attachment.setFileName(fileName);
                    attachment.setContentType(file.getContentType());
                    attachment.setData(fileData);
                    attachment.setTask(task);

                    attachmentService.save(attachment);

                } catch (IOException e) {
                    System.out.println("Error reading file: " + fileName);
                }
            }
        }

        return "redirect:/dashboard";
    }


}
