package com.example.demo.beans;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UserMB implements Serializable {
    @EJB
    private UserService userService;
    private User organizer;

    public UserMB() {
    }
    @PostConstruct
    public void init() {
        organizer = new User();
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public boolean isExistsUserWithEmail(String email) {
        User user = userService.findByEmail(email);
        return user != null;
    }

    // add an organizer
    public String addOrganizer()
    {
        if (!isExistsUserWithEmail(organizer.getEmail())) {
            organizer.setRole(Role.ORGANIZER);
            userService.register(organizer);
            return "admin";
        }else{
            return "admin?error=duplicate";
        }
    }

    // get all the organizers
    public List<User> getAllOrganizers()
    {
        return userService.allOrganizers();
    }

}
