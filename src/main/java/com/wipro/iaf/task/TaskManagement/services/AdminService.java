package com.wipro.iaf.task.TaskManagement.services;


import java.util.List;
import com.wipro.iaf.task.TaskManagement.entity.User;

public interface AdminService {
    List<User> getPendingManagerRequests();
    void approveManagerRole(Long userId);
    void rejectManagerRole(Long userId);
}
