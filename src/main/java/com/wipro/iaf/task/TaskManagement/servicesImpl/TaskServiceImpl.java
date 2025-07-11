package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Status;
import com.wipro.iaf.task.TaskManagement.repo.TaskRepository;
import com.wipro.iaf.task.TaskManagement.services.TaskService;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserService userService;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

  

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }
    @Override
    public long countByStatus(Status status) {
        return taskRepository.countByStatus(status);
    }
    
    @Override
    public Task getTaskWithDetails(Long id) throws Exception {
        Task task= taskRepository.findWithDetailsById(id).orElseThrow(() -> new Exception("Task not found"));
        
        Hibernate.initialize(task.getComments());
        Hibernate.initialize(task.getLogs());
        
        return task;
    }


    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(Status.valueOf(status));
    }

    @Override
    public List<Task> getOverdueTasks() {
        return taskRepository.findByDeadlineBeforeAndStatusNotIn(LocalDate.now(),List.of(Status.DONE,Status.ON_HOLD));
    }

    @Override
    public List<Task> getUpcomingTasks() {
        return taskRepository.findByDeadlineBetween(LocalDate.now(), LocalDate.now().plusDays(7));
    }
    
    public void updateTaskStatus(Long taskId, Status newStatus, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!task.getAssignedUsers().contains(user)) throw new AccessDeniedException("Not your task");
        task.setStatus(newStatus);
        task.setUpdatedDate(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Override
    public void submitTaskForReview(Long taskId, User user,Integer actualHours) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        if (!task.getAssignedUsers().contains(user)) throw new AccessDeniedException("Not your task");
        task.setStatus(Status.IN_REVIEW);
        task.setUpdatedDate(LocalDateTime.now());
        task.setActualHours(actualHours);
        taskRepository.save(task);

    }
    
    @Override
    public List<User> getAssignedUsers(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        return task.getAssignedUsers();
    }



	@Override
	public List<Task> getTasksAssignedToUsers(User user) {
		return taskRepository.findAllByAssignedUsersContains(user);
	}



	@Override
	public long countByStatusAndUserId(Status todo, Long id) {
		return taskRepository.countByStatusAndUserId(todo, id);
	}



	@Override
	public long countByStatusAndManagerId(Status todo, Long id) {
		return taskRepository.countByStatusAndManagerId(todo, id);
	}
	
	@Override
	public List<Task> getTasksAssignedToUser(User user) {
	    return taskRepository.findByAssignedUsersContaining(user);
	}

	@Override
	public List<Task> getTasksInReviewByManager(User manager) {
	    return taskRepository.findByCreatedByAndStatus(manager.getId(), Status.IN_REVIEW);
	}


	@Override
	public List<Task> getOverdueTasksAssignedByManager(User manager) {
	    LocalDate today = LocalDate.now();
	    List<Status> excludedStatuses = List.of(Status.DONE, Status.ON_HOLD);
	    return taskRepository.findByCreatedByAndDeadlineBeforeAndStatusNotIn(manager.getId(), today, excludedStatuses);
	}


	@Override
	public List<Task> getTasksForEmployeeUnderManager(Long employeeId, Long managerId) {
	    User employee = userService.getUserById(employeeId).get();
	    return taskRepository.findByAssignedUsersContainingAndCreatedBy(employee, managerId);
	}



	@Override
	public void updateStatus(Long taskId, Status status) {
		Optional<Task> task=taskRepository.findById(taskId);
		
		if(task.isPresent()) {
			Task taskRel=task.get();
			
			taskRel.setStatus(status);
			taskRel.setUpdatedDate(LocalDateTime.now());
			taskRepository.save(taskRel);
		}
		
	}
	
	@Override
	public Map<String, Long> getWeeklyDeadlineStats(User user) {
	    List<Task> allTasks = taskRepository.findAllByCreatedBy(user.getId());

	    LocalDate today = LocalDate.now();
	    LocalDate sevenDaysLater = today.plusDays(6); // 7 days including today

	    // Dynamically build upcoming 7 days in order
	    Map<String, Long> dayCountMap = new LinkedHashMap<>();
	    for (int i = 0; i < 7; i++) {
	        LocalDate date = today.plusDays(i);
	        DayOfWeek day = date.getDayOfWeek();
	        String shortDay = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
	        shortDay = shortDay.substring(0, 1).toUpperCase() + shortDay.substring(1, 3).toLowerCase(); // Mon, Tue...
	        dayCountMap.put(shortDay, 0L);
	    }

	    // Count deadlines per day
	    for (Task task : allTasks) {
	        LocalDate deadline = task.getDeadline();
	        if (deadline != null &&
	            task.getStatus() != Status.DONE &&
	            !deadline.isBefore(today) &&
	            !deadline.isAfter(sevenDaysLater)) {

	            DayOfWeek day = deadline.getDayOfWeek();
	            String shortDay = day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
	            shortDay = shortDay.substring(0, 1).toUpperCase() + shortDay.substring(1, 3).toLowerCase();

	            // Safeguard: Only count if the deadline falls in the next 7-day range
	            if (dayCountMap.containsKey(shortDay)) {
	                dayCountMap.put(shortDay, dayCountMap.get(shortDay) + 1);
	            }
	        }
	    }

	    return dayCountMap;
	}



    

}