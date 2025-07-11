package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.Comment;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.repo.CommentRepository;
import com.wipro.iaf.task.TaskManagement.services.CommentService;
import com.wipro.iaf.task.TaskManagement.services.TaskService;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Override
	public List<Comment> getCommentsForTask(Long taskId) {
	    return commentRepository.findByTaskIdOrderByCreatedDateAsc(taskId);
	}
	
	@Override
	public List<Comment> getCommentsForTask(Task task) {
	    return commentRepository.findByTaskIdOrderByCreatedDateAsc(task.getId());
	}

	@Override
	public boolean addComment(Long taskId, User user, String content) {
		
		try {
			Task task=taskService.getTaskWithDetails(taskId);
			Comment comment=new Comment();
			comment.setAuthor(user);
			comment.setCreatedDate(task.getCreatedDate());
			comment.setTask(task);
			comment.setMessage(content);
			comment.setTimestamp(LocalDateTime.now());
			
			commentRepository.save(comment);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}


}
