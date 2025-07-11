package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;

import com.wipro.iaf.task.TaskManagement.entity.Comment;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;

public interface CommentService {
	
	List<Comment> getCommentsForTask(Long taskId);

	List<Comment> getCommentsForTask(Task task);

	boolean addComment(Long taskId, User user, String content);


}
