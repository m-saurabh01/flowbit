package com.wipro.iaf.task.TaskManagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.iaf.task.TaskManagement.entity.Comment;
import com.wipro.iaf.task.TaskManagement.entity.Task;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByTaskIdOrderByCreatedDateAsc(Task task);

	List<Comment> findByTaskIdOrderByCreatedDateAsc(Long taskId);
}
