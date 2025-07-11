package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.repo.UserRepository;
import com.wipro.iaf.task.TaskManagement.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired private UserRepository userRepository;

    @Override
    public List<User> getPendingManagerRequests() {
        return userRepository.findByPendingManagerApprovalTrue();
    }

    @Override
    public void approveManagerRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setRole(Role.MANAGER);
        user.setPendingManagerApproval(false);
        userRepository.save(user);
    }

    @Override
    public void rejectManagerRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setPendingManagerApproval(false);
        userRepository.save(user);
    }
}