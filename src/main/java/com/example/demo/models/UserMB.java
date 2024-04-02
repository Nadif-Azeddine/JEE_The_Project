package com.example.demo.models;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class UserMB implements Serializable {
    @EJB
    private UserService userService;

    public UserMB() {
    }

    public boolean isExistsUserWithEmail(String email) {
        User user = userService.findByEmail(email);
        return user != null;
    }
}
