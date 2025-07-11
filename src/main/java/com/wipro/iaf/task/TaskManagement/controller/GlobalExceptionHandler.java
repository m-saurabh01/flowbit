package com.wipro.iaf.task.TaskManagement.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(HttpServletRequest request, Model model) {
        model.addAttribute("path", request.getRequestURI());
        return "error/403";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(HttpServletRequest request, Model model) {
        model.addAttribute("path", request.getRequestURI());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        System.out.println("Unhandled exception at URI: {} "+ request.getRequestURI()+" ");
        ex.printStackTrace();
        return "error/500";
    }
}