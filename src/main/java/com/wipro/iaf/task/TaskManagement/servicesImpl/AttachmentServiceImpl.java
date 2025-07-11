package com.wipro.iaf.task.TaskManagement.servicesImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.iaf.task.TaskManagement.entity.Attachment;
import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.repo.AttachmentRepository;
import com.wipro.iaf.task.TaskManagement.services.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService{
	
	@Autowired
    private AttachmentRepository attachmentRepository;

	@Override
	public void save(Attachment attachment) {
        attachmentRepository.save(attachment);
    }
	@Override
    public List<Attachment> getAttachmentsByTask(Task task) {
        return attachmentRepository.findByTask(task);
    }
	@Override
    public Optional<Attachment> getById(Long id) {
        return attachmentRepository.findById(id);
    }
	@Override
    public void delete(Long id) {
        attachmentRepository.deleteById(id);
    }

}
