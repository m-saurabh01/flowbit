package com.wipro.iaf.task.TaskManagement.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;


@Entity
@Data
public class MappingRequest {
 @Id @GeneratedValue
 private Long id;

 @ManyToOne
 private User employee;

 @ManyToOne
 private User requestedManager;

 private LocalDateTime requestDate;
 private boolean approved;
}
