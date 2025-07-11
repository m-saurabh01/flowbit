package com.wipro.iaf.task.TaskManagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;

import com.wipro.iaf.task.TaskManagement.enums.Priority;
import com.wipro.iaf.task.TaskManagement.enums.Status;

import lombok.Getter;
import lombok.Setter;



@NamedEntityGraph(
	    name = "Task.detailView",
	    attributeNodes = {
	        @NamedAttributeNode("attachments")  
	    }
	)
	@Entity
	@Getter
	@Setter
	public class Task {
	    @Id
	    @GeneratedValue
	    private Long id;

	    private String title;
	    private String description;

	    @ManyToMany
	    @JoinTable(
	        name = "task_user_mapping",
	        joinColumns = @JoinColumn(name = "task_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id")
	    )
	    private List<User> assignedUsers;

	    @Enumerated(EnumType.STRING)
	    private Priority priority;

	    @Enumerated(EnumType.STRING)
	    private Status status;

	    private String tags;
	    private Integer estimatedHours;
	    private Integer actualHours;

	    private LocalDate deadline;
	    private LocalDateTime createdDate;
	    private LocalDateTime updatedDate;
	    
	    private Long createdBy;

	    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private Set<Attachment> attachments = new HashSet<>();

	    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private Set<Comment> comments = new HashSet<>();

	    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    private Set<DailyLog> logs = new HashSet<>();
	}
