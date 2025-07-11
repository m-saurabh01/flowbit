package com.wipro.iaf.task.TaskManagement.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Priority;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.enums.Status;
import com.wipro.iaf.task.TaskManagement.services.CommentService;
import com.wipro.iaf.task.TaskManagement.services.DailyLogService;
import com.wipro.iaf.task.TaskManagement.services.MappingRequestService;
import com.wipro.iaf.task.TaskManagement.services.TaskService;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Controller
public class DashboardController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@Autowired
	private DailyLogService dailyLogService;

	@Autowired
	private CommentService commentService;

	@Autowired
	MappingRequestService mappingRequestService;

	@GetMapping("/dashboard")
	public String showDashboard(Authentication authentication, Model model) {

		// 1. Get the logged-in user
		String email = authentication.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);

		if (user.getRole() == Role.ADMIN) {
			return "redirect:/admin/dashboard";
		}

		// 2. Get all tasks based on role
		List<Task> tasks;
		if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER) {
			tasks = taskService.getAllTasks();
		} else {
			tasks = taskService.getTasksAssignedToUser(user);
		}

		// 3. Task counts by status
		Map<Status, Long> statusCounts = tasks.stream()
				.collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

		model.addAttribute("todoCount", statusCounts.getOrDefault(Status.TODO, 0L));
		model.addAttribute("inProgressCount", statusCounts.getOrDefault(Status.IN_PROGRESS, 0L));
		model.addAttribute("inReviewCount", statusCounts.getOrDefault(Status.IN_REVIEW, 0L));
		model.addAttribute("doneCount", statusCounts.getOrDefault(Status.DONE, 0L));

		// 4. Overdue tasks (deadline before today)

		// 5. Upcoming tasks (deadline within next 7 days)
		List<Task> upcomingTasks = tasks.stream().filter(t -> t.getDeadline() != null
				&& !t.getDeadline().isBefore(LocalDate.now()) && t.getDeadline().isBefore(LocalDate.now().plusDays(7)))
				.collect(Collectors.toList());
		model.addAttribute("upcomingTasks", upcomingTasks);

		// 7. Pie Chart Data
		List<String> chartLabels = List.of("To Do", "In Progress", "In Review", "Done", "On Hold", "Not Started");

		model.addAttribute("chartLabels", chartLabels);

		boolean alreadyMapped = user.getManager() != null;

		boolean requestPending = mappingRequestService.existsByEmployee(user);

		model.addAttribute("alreadyMapped", alreadyMapped);
		model.addAttribute("requestPending", requestPending);
		model.addAttribute("availableManagers", userService.getAllManagers());

		// 8. Return correct dashboard based on role
		if (user.getRole() == Role.MANAGER) {

			// 1. Chart data (based on tasks the manager assigned)
			List<Long> chartValues = Arrays.asList(taskService.countByStatusAndManagerId(Status.TODO, user.getId()),
					taskService.countByStatusAndManagerId(Status.IN_PROGRESS, user.getId()),
					taskService.countByStatusAndManagerId(Status.IN_REVIEW, user.getId()),
					taskService.countByStatusAndManagerId(Status.DONE, user.getId()),
					taskService.countByStatusAndManagerId(Status.ON_HOLD, user.getId()),
					taskService.countByStatusAndManagerId(Status.NOT_STARTED, user.getId()));
			model.addAttribute("chartLabels",
					List.of("To Do", "In Progress", "In Review", "Done", "On Hold", "Not Started"));
			model.addAttribute("chartValues", chartValues);

			// 2. Overdue tasks assigned by manager
			List<Task> overdueTasks = taskService.getOverdueTasksAssignedByManager(user);
			model.addAttribute("overdueTasks", overdueTasks);
			

			// 3. Tasks assigned TO manager (their own work)
			List<Task> myTasks = taskService.getTasksAssignedToUser(user);
			model.addAttribute("myTasks", myTasks);

			// 4. Tasks submitted for review
			List<Task> tasksForReview = taskService.getTasksInReviewByManager(user);
			model.addAttribute("tasksForReview", tasksForReview);

			// 5. Employees under manager and their tasks
			List<User> team = userService.getEmployeesUnderManager(user.getEmail());
			Map<Long, List<Task>> employeeTasks = new HashMap<>();
			for (User emp : team) {
				employeeTasks.put(emp.getId(), taskService.getTasksAssignedToUser(emp));
			}
			model.addAttribute("team", team);
			model.addAttribute("employeeTasks", employeeTasks);
			
			Map<String, Long> deadlineChartData = taskService.getWeeklyDeadlineStats(user);
			
		    
		    model.addAttribute("deadlineLabels", deadlineChartData.keySet());
		    model.addAttribute("deadlineCounts", deadlineChartData.values());

			return "dashboard/admin";
		} else {
			List<Task> myTasks = taskService.getTasksAssignedToUser(user);
			model.addAttribute("myTasks", myTasks);
			Map<String, Long> priorityCounts = myTasks.stream()
			        .collect(Collectors.groupingBy(
			            t -> t.getPriority().name(), Collectors.counting()
			        ));
			List<String> labels = Arrays.stream(Priority.values())
			        .map(Priority::name)
			        .collect(Collectors.toList());

			List<Long> values = labels.stream()
			        .map(label -> priorityCounts.getOrDefault(label, 0L))
			        .collect(Collectors.toList());

			model.addAttribute("priorityChartLabels", labels);
			model.addAttribute("priorityChartValues", values);
			

			List<Long> chartValues = Arrays.asList(taskService.countByStatusAndUserId(Status.TODO, user.getId()),
					taskService.countByStatusAndUserId(Status.IN_PROGRESS, user.getId()),
					taskService.countByStatusAndUserId(Status.IN_REVIEW, user.getId()),
					taskService.countByStatusAndUserId(Status.DONE, user.getId()),
					taskService.countByStatusAndUserId(Status.ON_HOLD, user.getId()),
					taskService.countByStatusAndUserId(Status.NOT_STARTED, user.getId()));
			model.addAttribute("chartValues", chartValues);
			return "dashboard/employee";
		}

	}

	@GetMapping("/tasks/{id}")
	public String viewTask(@PathVariable Long id, Model model, Authentication auth) {
		Task task = taskService.getTaskById(id).orElseThrow(() -> new RuntimeException("Task not found"));
		User user = userService.findByEmail(auth.getName());
		boolean isAutorisedToSee=task.getAssignedUsers().stream().anyMatch(a->a.getId()==user.getId());
		
		if (user.getId().equals(task.getCreatedBy())||isAutorisedToSee) {
	        
	    

		model.addAttribute("task", task);
		model.addAttribute("user", user);
		model.addAttribute("comments", commentService.getCommentsForTask(task));
		model.addAttribute("logs", dailyLogService.getLogsForTask(task));
		Set<Long> assignedUserIds = task.getAssignedUsers().stream().map(User::getId).collect(Collectors.toSet());

		model.addAttribute("isAssignedUserId", assignedUserIds.contains(user.getId()));
		model.addAttribute("createdBy",userService.getUserById(task.getCreatedBy()).get().getName());

		model.addAttribute("statusOptions",
				List.of(Status.NOT_STARTED, Status.IN_PROGRESS, Status.ON_HOLD, Status.TODO));

		return "tasks/view";
		}
		return "error/403"; 
	}

	@PostMapping("/tasks/update-status")
	public String updateStatus(@RequestParam Long taskId, @RequestParam Status status, Authentication auth) {
		User user = userService.findByEmail(auth.getName());
		taskService.updateTaskStatus(taskId, status, user);
		return "redirect:/tasks/" + taskId;
	}

	@PostMapping("/tasks/submit-review")
	public String submitForReview(@RequestParam Long taskId,@RequestParam Integer actualHours, Authentication auth) {
		User user = userService.findByEmail(auth.getName());
		taskService.submitTaskForReview(taskId, user,actualHours);
		return "redirect:/tasks/" + taskId;
	}

	@PostMapping("/tasks/add-comment")
	public String addComment(@RequestParam Long taskId, @RequestParam String content, Authentication auth) {
		User user = userService.findByEmail(auth.getName());
		String cleanHtml = Jsoup.clean(content, Safelist.basicWithImages());
		commentService.addComment(taskId, user, cleanHtml);
		System.out.println("Saved");
		return "redirect:/tasks/" + taskId;
	}

	@PostMapping("/tasks/add-log")
	public String addLog(@RequestParam Long taskId, @RequestParam String note, @RequestParam int hoursSpent,
			Authentication auth) {
		User user = userService.findByEmail(auth.getName());
		dailyLogService.addLog(taskId, user, note, hoursSpent);
		return "redirect:/tasks/" + taskId;
	}

	@PostMapping("/tasks/approve")
	public String approveTask(@RequestParam Long taskId) {
		taskService.updateStatus(taskId, Status.DONE);
		return "redirect:/dashboard"; // or wherever the manager dashboard is
	}

	@PostMapping("/tasks/reject")
	public String rejectTask(@RequestParam Long taskId) {
		taskService.updateStatus(taskId, Status.TODO);
		return "redirect:/dashboard"; // or back to approval list
	}
	
	@GetMapping("/manager/employee/{employeeId}")
	public String showEmployeeDetails(@PathVariable Long employeeId, Model model, Authentication authentication) {
	    User manager = userService.findByEmail(authentication.getName());

	    User employee = userService.getUserById(employeeId).get();
	    
	    if (!employee.getManager().getId().equals(manager.getId())) {
	        return "error/403"; 
	    }

	    List<Task> assignedTasks = taskService.getTasksAssignedToUser(employee);

	    model.addAttribute("employee", employee);
	    model.addAttribute("tasks", assignedTasks);
	    return "manager/employee-details"; // JSP
	}


}
