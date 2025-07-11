package com.wipro.iaf.task.TaskManagement.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class AuditLog {
    @Id @GeneratedValue
    private Long id;

    private String action;
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    @ManyToOne
    private Task task;
}
