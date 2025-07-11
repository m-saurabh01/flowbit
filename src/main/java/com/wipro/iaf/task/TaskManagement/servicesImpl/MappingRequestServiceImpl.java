package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.MappingRequest;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.repo.MappingRequestRepository;
import com.wipro.iaf.task.TaskManagement.repo.UserRepository;
import com.wipro.iaf.task.TaskManagement.services.MappingRequestService;

@Service
public class MappingRequestServiceImpl implements MappingRequestService {
    @Autowired private MappingRequestRepository mappingRepo;
    @Autowired private UserRepository userRepo;

    @Override
    public String createMappingRequest(String employeeEmail, Long managerId) {
        User employee = userRepo.findByEmail(employeeEmail).orElseThrow();
        User manager = userRepo.findById(managerId).orElseThrow();

        if (mappingRepo.existsByEmployeeAndApprovedFalse(employee)) {
            return "You already have a pending mapping request.";
        }

        MappingRequest req = new MappingRequest();
        req.setEmployee(employee);
        req.setRequestedManager(manager);
        req.setRequestDate(LocalDateTime.now());
        req.setApproved(false);
        mappingRepo.save(req);
        return "Mapping request sent to Admin.";
    }

    @Override
    public void approveRequest(Long requestId) {
        MappingRequest req = mappingRepo.findById(requestId).orElseThrow();
        User emp = req.getEmployee();
        emp.setManager(req.getRequestedManager());
        req.setApproved(true);
        userRepo.save(emp);
        mappingRepo.save(req);
    }
    
    @Override
    public List<MappingRequest> getPendingRequests() {
        return mappingRepo.findByApprovedFalse();
    }

	@Override
	public boolean existsByEmployee(User user) {
		return mappingRepo.existsByEmployeeAndApprovedFalse(user);
	}

	@Override
	public void deleteById(Long requestId) {
		mappingRepo.deleteById(requestId);
		
	}
}

