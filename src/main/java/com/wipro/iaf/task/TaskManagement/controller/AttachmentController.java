package com.wipro.iaf.task.TaskManagement.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.iaf.task.TaskManagement.entity.Attachment;
import com.wipro.iaf.task.TaskManagement.repo.AttachmentRepository;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;

    public AttachmentController(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
