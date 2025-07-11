package com.wipro.iaf.task.TaskManagement.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class DailyLog {
    @Id @GeneratedValue
    private Long id;

    private String progressNote;

    private LocalDateTime date;
    
    private int hourSpent;

    @ManyToOne
    private User user;

    @ManyToOne
    private Task task;
}
