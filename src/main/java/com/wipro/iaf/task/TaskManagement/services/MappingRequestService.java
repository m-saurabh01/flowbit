package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;

import com.wipro.iaf.task.TaskManagement.entity.MappingRequest;
import com.wipro.iaf.task.TaskManagement.entity.User;

public interface MappingRequestService {
	
	public List<MappingRequest> getPendingRequests();
	public void approveRequest(Long requestId);
	public String createMappingRequest(String employeeEmail, Long managerId) ;
	public boolean existsByEmployee(User user);
	public void deleteById(Long requestId);
	

}
