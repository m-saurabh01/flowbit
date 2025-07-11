package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Status;

public interface TaskService {
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(Long id);
    List<Task> getTasksByStatus(String status);
    List<Task> getOverdueTasks();
    List<Task> getUpcomingTasks();
	long countByStatus(Status status);
	Task getTaskWithDetails(Long id) throws Exception;
	void updateTaskStatus(Long taskId, Status status, User user);
	void submitTaskForReview(Long taskId, User user, Integer actualHours);
	List<User> getAssignedUsers(Long taskId);
	List<Task> getTasksAssignedToUser(User user);
	long countByStatusAndUserId(Status todo, Long id);
	long countByStatusAndManagerId(Status todo, Long id);
	List<Task> getTasksAssignedToUsers(User user);
	List<Task> getTasksInReviewByManager(User manager);
	List<Task> getOverdueTasksAssignedByManager(User manager);
	List<Task> getTasksForEmployeeUnderManager(Long employeeId, Long managerId);
	void updateStatus(Long taskId, Status done);
	Map<String, Long> getWeeklyDeadlineStats(User user);
}