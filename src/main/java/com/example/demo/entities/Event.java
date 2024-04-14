package com.example.demo.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String imagePath;

    @Column(nullable = true)
    private String mediaSupport;

    @Column(nullable = true)
    private String color;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @ManyToMany
    @JoinTable(
            name = "organizer_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> organizers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "participates_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participates = new HashSet<>();


    public Event() {
    }

    public Event(String title, String description, String location, String imagePath, String mediaSupport, String color, Date startDate, Date endDate, EventType type) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.imagePath = imagePath;
        this.mediaSupport = mediaSupport;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public Event(Long id, String title, String description, String location, String imagePath, String mediaSupport, String color, Date startDate, Date endDate, EventType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.imagePath = imagePath;
        this.mediaSupport = mediaSupport;
        this.color = color;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    // Getters and setters for all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMediaSupport() {
        return mediaSupport;
    }

    public void setMediaSupport(String mediaSupport) {
        this.mediaSupport = mediaSupport;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Set<User> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(Set<User> organizers) {
        this.organizers = organizers;
    }

    public Set<User> getParticipates() {
        return participates;
    }

    public void setParticipates(Set<User> participates) {
        this.participates = participates;
    }
}
