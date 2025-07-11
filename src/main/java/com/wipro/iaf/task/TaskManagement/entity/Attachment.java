package com.wipro.iaf.task.TaskManagement.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Attachment {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String fileName;
	    private String contentType;

	    @Lob
	    private byte[] data;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "task_id")
	    private Task task;
	

}
