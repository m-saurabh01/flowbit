package com.wipro.iaf.task.TaskManagement.dto;



import java.util.List;

import com.wipro.iaf.task.TaskManagement.enums.Priority;
import com.wipro.iaf.task.TaskManagement.enums.Status;

import lombok.Data;

@Data
public class TaskDto {
    private String title;
    private String description;
    private List<Long> assignedUserIds; 
    private Priority priority;
    private Status status;
    private String tags;
    private Integer estimatedHours;
    private Integer actualHours;
    private String deadline;
}
