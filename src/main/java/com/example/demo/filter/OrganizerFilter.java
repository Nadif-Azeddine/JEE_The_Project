package com.example.demo.filter;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "OrganizerFilter", urlPatterns = { "/organizer/*" })
public class OrganizerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession session = reqt.getSession(false);

            // Check if the user is logged in and has admin role
            if (session != null && session.getAttribute("user") != null) {
                User loggedUser = (User) session.getAttribute("user");
                if (loggedUser.getRole().equals(Role.ORGANIZER)) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            // Redirect to error page  not admin
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.sendRedirect(reqt.getContextPath() + "/401.xhtml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
