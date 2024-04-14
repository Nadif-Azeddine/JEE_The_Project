package com.example.demo.services;

import com.example.demo.entities.Event;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
@Local
public class UserService {
    EntityManagerFactory emf;
    EntityManager em;

    public UserService() {
        emf = Persistence.createEntityManagerFactory("EventPU");
        em = emf.createEntityManager();
    }

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public void register(User user) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(user);
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String email, String password) {
        try{
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
        if (user != null) {
            return user;
        }
    } catch( NoResultException e)
    {
        return null;
    }
    return null;
}

    public User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email ", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null;
        }
    }

    public List<User> allOrganizers() {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.role = :roleName", User.class);
            query.setParameter("roleName", Role.ORGANIZER);
            return query.getResultList();
        } catch (NoResultException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Event> allParticipatedEvents(User user) {
            String jpql = "SELECT e FROM Event e JOIN e.participates u WHERE u.id = :userId";

            TypedQuery<Event> query = em.createQuery(jpql, Event.class);
            query.setParameter("userId", user.getId());
            return query.getResultList();
    }
}
