package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;
import java.util.Optional;

import com.wipro.iaf.task.TaskManagement.entity.User;

public interface UserService {

	User findByEmail(String email);

	Optional<User> getUserById(Long assignedUserId);

	List<User> getAllUsers();

	String updateUserProfile(User updatedUser, String email);

	List<User> getUsersPendingManagerApproval();

	void approveManagerRole(Long userId);

	void deleteUser(Long userId);

	void save(User user);

	List<User> getEmployeesUnderManager(String email);

	List<User> getAllManagers();
	
	List<User> getAssignedEmployees(Long managerId);

	List<User> getUsersPendingManagerMappingApproval();

	List<User> getAllCustomManagers(User user);

	void updateUserPassword(Long userId, String newPassword);





}
