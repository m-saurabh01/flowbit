package com.wipro.iaf.task.TaskManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.iaf.task.TaskManagement.dto.AuthRequest;
import com.wipro.iaf.task.TaskManagement.dto.AuthResponse;
import com.wipro.iaf.task.TaskManagement.dto.SignupRequest;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Role;
import com.wipro.iaf.task.TaskManagement.repo.UserRepository;
import com.wipro.iaf.task.TaskManagement.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
    	
        if (userRepo.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        
        User newUser = new User();
        newUser.setName(signupRequest.getName());
        newUser.setEmail(signupRequest.getEmail());
        newUser.setRole(Role.EMPLOYEE);
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepo.save(newUser);

        String token = jwtUtil.generateToken(signupRequest.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}



