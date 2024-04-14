package com.example.demo.services;

import com.example.demo.entities.Event;
import com.example.demo.entities.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Stateless
@Local
public class EventService {

    EntityManagerFactory emf;
    EntityManager em;

    public EventService() {
        emf = Persistence.createEntityManagerFactory("EventPU");
        em = emf.createEntityManager();
    }

    public Event saveEvent(Event event) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(event);
            et.commit();
            return event;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Event updateEvent(Event event) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            event = em.merge(event); // Merge the detached object
            et.commit();
            return event;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteEvent(Long eventId) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Event event = em.find(Event.class, eventId);
            if (event != null) {
                em.remove(event);
            }
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Event> findEventsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.startDate BETWEEN :startDate AND :endDate");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<Event> findEventsByName(String name) {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.title LIKE :name");
        query.setParameter("name", "%" + name + "%"); // Search by name containing the provided string
        return query.getResultList();
    }

    public List<Event> getAll() {

        try {
            Query query = em.createQuery("SELECT e FROM Event e");
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    // get all the events with their organizers
    public List<Event> getAllWithOrganizers() {
        try {
            Query query = em.createQuery("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.organizers");
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    public List<Event> getEventsByOrganizer(User organizer) {
        String jpql = "SELECT e FROM Event e JOIN e.organizers u WHERE u.id = :organizerId";

        TypedQuery<Event> query = em.createQuery(jpql, Event.class);
        query.setParameter("organizerId", organizer.getId());

        return query.getResultList();
    }

    // add and remove organizer
    public void attachOrganizerToEvent(Event event, Long organizerId) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            User organizer = em.find(User.class, organizerId);
            if (event != null && organizer != null) {
                event.getOrganizers().add(organizer);
                organizer.getOrganizedEvents().add(event);
            }
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeOrganizerFromEvent(Event event, Long organizerId) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            User organizer = em.find(User.class, organizerId);
            if (event != null && organizer != null) {
                event.getOrganizers().remove(organizer);
                organizer.getOrganizedEvents().remove(event);
            }
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // add and remove event to participate list
    public void addEventToParticipate(Long eventId, Long organizerId) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Event event = em.find(Event.class, eventId);
            User organizer = em.find(User.class, organizerId);
            if (event != null && organizer != null) {
                event.getParticipates().add(organizer);
                organizer.getParticipedEvents().add(event);
            }
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeEventFromParticipates(Long eventId, Long organizerId) {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Event event = em.find(Event.class, eventId);
            User organizer = em.find(User.class, organizerId);
            if (event != null && organizer != null) {
                event.getParticipates().remove(organizer);
                organizer.getParticipedEvents().remove(event);
            }
            et.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
