package com.wipro.iaf.task.TaskManagement.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Comment {
    @Id @GeneratedValue
    private Long id;

    private String message;
    private LocalDateTime timestamp;

    @ManyToOne
    private User author;

    @ManyToOne
    private Task task;
    
    private LocalDateTime createdDate;
}

