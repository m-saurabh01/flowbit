package com.wipro.iaf.task.TaskManagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.iaf.task.TaskManagement.entity.Attachment;
import com.wipro.iaf.task.TaskManagement.entity.Task;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
	
	List<Attachment> findByTask(Task task);
}
