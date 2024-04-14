package com.example.demo.beans;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;

@Named
@SessionScoped
public class AuthMB implements Serializable {
    @EJB
    private UserService userService;
    private User loggedUser;
    // params
    private User userReg;
    private String email;
    private String password;

    public AuthMB() {
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public User getUserReg() {
        return userReg;
    }

    public void setUserReg(User userReg) {
        this.userReg = userReg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PostConstruct
    public void init(){
        userReg = new User();
    }

    public String register()
    {

        if (userService.findByEmail(email) != null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","The provided email is already used."));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "register";
        }
        userService.register(userReg);
        this.loggedUser = userReg;
        HttpSession session = SessionUtils.getSession();
        session.setAttribute("user", userReg);
        return "home";
    }

    public String login()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("###########################" + password + email);
        User userCheck = userService.login(email, password);
        if(userCheck != null)
        {
            this.loggedUser = userCheck;
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("user", userCheck);
            return "home";
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"", "Wrong email or password"));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "login";
        }
    }

    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        this.loggedUser = null;
        return "login";
    }

    public boolean isAdmin(){
        return this.loggedUser != null && this.loggedUser.getRole().equals(Role.ADMIN);
    }

    public boolean isOrganizer(){
        return this.loggedUser != null && this.loggedUser.getRole().equals(Role.ORGANIZER);
    }

}
