package com.wipro.iaf.task.TaskManagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.iaf.task.TaskManagement.entity.MappingRequest;
import com.wipro.iaf.task.TaskManagement.entity.User;

public interface MappingRequestRepository extends JpaRepository<MappingRequest, Long> {
    List<MappingRequest> findByApprovedFalse();
    boolean existsByEmployeeAndApprovedFalse(User employee);
	List<MappingRequest> findAllByApprovedFalse();
    
    
}
