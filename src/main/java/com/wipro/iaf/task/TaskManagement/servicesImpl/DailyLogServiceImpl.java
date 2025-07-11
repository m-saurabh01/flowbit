package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wipro.iaf.task.TaskManagement.entity.DailyLog;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.repo.DailyLogRepository;
import com.wipro.iaf.task.TaskManagement.services.DailyLogService;
import com.wipro.iaf.task.TaskManagement.services.TaskService;

@Service
public class DailyLogServiceImpl implements DailyLogService{
	
	@Autowired
	private DailyLogRepository logRepository;
	
	@Autowired
	private TaskService taskService;

	@Override
	public List<DailyLog> getLogsForTask(Task task) {
		return logRepository.findLogsByTask(task.getId());
	}

	@Override
	public boolean addLog(Long taskId, User user, String note, int hoursSpent) {
		
		try {
			Task task=taskService.getTaskWithDetails(taskId);
			DailyLog logs=new DailyLog();
			logs.setUser(user);
			logs.setDate(LocalDateTime.now());
			logs.setTask(task);
			logs.setProgressNote(note);
			logs.setHourSpent(hoursSpent);
			
			logRepository.save(logs);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
