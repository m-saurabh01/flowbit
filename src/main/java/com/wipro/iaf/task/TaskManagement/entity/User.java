package com.wipro.iaf.task.TaskManagement.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.wipro.iaf.task.TaskManagement.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String password;
    private boolean pendingManagerApproval;
    private boolean active = true;
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @OneToMany(mappedBy = "manager")
    private List<User> managedEmployees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;


}
