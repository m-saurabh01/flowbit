package com.wipro.iaf.task.TaskManagement.services;

import java.util.List;
import java.util.Optional;

import com.wipro.iaf.task.TaskManagement.entity.Attachment;
import com.wipro.iaf.task.TaskManagement.entity.Task;

public interface AttachmentService {
	
	public void save(Attachment attachment);

    public List<Attachment> getAttachmentsByTask(Task task);

    public Optional<Attachment> getById(Long id);

    public void delete(Long id);
}
