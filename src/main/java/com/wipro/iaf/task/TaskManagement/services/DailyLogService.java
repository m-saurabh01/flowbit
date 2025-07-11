package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;

import com.wipro.iaf.task.TaskManagement.entity.DailyLog;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;

public interface DailyLogService {
	
	List<DailyLog> getLogsForTask(Task task);

	boolean addLog(Long taskId, User user, String note, int hoursSpent);

}
