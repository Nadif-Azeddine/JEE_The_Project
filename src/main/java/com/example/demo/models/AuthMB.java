package com.example.demo.models;

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
        FacesContext context = FacesContext.getCurrentInstance();
        if (userService.findByEmail(email) != null){
            System.out.println("#########################################################email exist ######################################");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","The provided email is already used."));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "register";
        }
        userService.register(userReg);
        return "home";
    }

    public String login()
    {
        FacesContext context = FacesContext.getCurrentInstance();

        User userCheck = userService.login(email, password);
        if(userCheck != null)
        {
            this.loggedUser = userCheck;
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("user", userCheck);
            return "home";
        }else{
            context.addMessage(null, new FacesMessage("Wrong email or password"));
            context.addMessage(null, new FacesMessage("Wrong email or password"));
            return "login";
        }
    }
    public String authenticate()
    {
        if(this.loggedUser != null) return "";
        return "login?faces-redirect=true";
    }
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        this.loggedUser = null;
        return "login?faces-redirect=true";
    }

}
