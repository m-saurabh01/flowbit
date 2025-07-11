package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.MappingRequest;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.repo.MappingRequestRepository;
import com.wipro.iaf.task.TaskManagement.repo.UserRepository;
import com.wipro.iaf.task.TaskManagement.services.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MappingRequestRepository mappingRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

	}

	@Override
	public Optional<User> getUserById(Long assignedUserId) {
		return userRepository.findById(assignedUserId);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	

	@Override
	public String updateUserProfile(User updatedUser, String email) {
	    User existingUser = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    existingUser.setName(updatedUser.getName());

	    // ✅ Handle password change only if it's filled
	    if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
	        String hashed = passwordEncoder.encode(updatedUser.getPassword());
	        existingUser.setPassword(hashed);
	    }

	    String message;
	    
	    System.out.println(updatedUser);

	    // ✅ Prevent ADMIN promotion
	    if (updatedUser.getRole() == Role.ADMIN) {
	        message = "You cannot request Admin role.";
	    } 
	    else if (updatedUser.getRole() == Role.MANAGER) {
	        // ✅ Check if already pending
	        if (!existingUser.getRole().equals(Role.MANAGER) && !existingUser.isPendingManagerApproval()) {
	            existingUser.setPendingManagerApproval(true);
	            existingUser.setRole(Role.EMPLOYEE); 
	            message = "Profile updated. Role request sent to Admin for approval.";
	        } else {
	            message = "Manager role request is already pending.";
	        }
	    } 
	    else {
	        existingUser.setRole(updatedUser.getRole()==null?Role.EMPLOYEE:updatedUser.getRole());
	        existingUser.setPendingManagerApproval(false);
	        message = "Profile updated successfully.";
	    }

	    userRepository.save(existingUser);
	    return message;
	}
	
	@Override
	public List<User> getEmployeesUnderManager(String managerEmail) {
	    User manager = userRepository.findByEmail(managerEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    List<User> employees = userRepository.findByManagerAndActiveTrue(manager);
	    return employees;
	}

	@Override
	public List<User> getUsersPendingManagerApproval() {
	    return userRepository.findByPendingManagerApprovalTrue();
	}
	
	@Override
	public List<User> getUsersPendingManagerMappingApproval() {
		List<MappingRequest> mappings=mappingRepository.findAllByApprovedFalse();
	    return mappings.stream().map(mp->mp.getEmployee()).collect(Collectors.toList());
	}

	@Override
	public void approveManagerRole(Long userId) {
	    User user = userRepository.findById(userId).orElseThrow();
	    user.setRole(Role.MANAGER);
	    user.setPendingManagerApproval(false);
	    userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public List<User> getAllManagers() {
		return userRepository.findAll().stream().filter(it->it.getRole().equals(Role.MANAGER)).collect(Collectors.toList());
	}
	
	@Override
	public List<User> getAssignedEmployees(Long managerId) {
	    return userRepository.findByManagerIdAndActiveTrue(managerId);
	}

	@Override
	public List<User> getAllCustomManagers(User user) {
		Long managerId = (user.getManager() != null) ? user.getManager().getId() : null;
	    Long userId = user.getId();
	    return userRepository.findCustomManagers(managerId, userId);
		}
	

	@Override
	public void updateUserPassword(Long userId, String newPassword) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setPassword(passwordEncoder.encode(newPassword));
	    userRepository.save(user);
	}

	}
	
	


