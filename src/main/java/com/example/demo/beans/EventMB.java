package com.example.demo.beans;

import com.example.demo.entities.Event;
import com.example.demo.entities.User;
import com.example.demo.services.EventService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Named
@RequestScoped
public class EventMB implements Serializable {
    @EJB
    private EventService eventService;
    @Inject
    private AuthMB authMB;
    @Inject
    private UserMB userMB;
    private Event event;

    // used to get the event id from the json events in the calendar
    // and pass it in the event to participed list
    private String participedEventId;

    private Long organizer;
    private Part file;

    @PostConstruct
    public void init(){
        event = new Event();
    }

    public EventMB() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getParticipedEventId() {
        return participedEventId;
    }

    public void setParticipedEventId(String participedEventId) {
        this.participedEventId = participedEventId;
    }

    public Long getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Long organizer) {
        this.organizer = organizer;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String save() {
      try {
          this.event.setImagePath("https://www.goldshipdz.com/l-fr/imgs/no-image.jpg");
         Event savedEvent = eventService.saveEvent(this.event);
         if (savedEvent == null) {
             return "admin?error=error saving event";
         }

         eventService.attachOrganizerToEvent(savedEvent, organizer);
         return "admin?success=event saves";

      } catch(Exception e) {
          e.printStackTrace();
          return "admin?error=error saving event";
      }
    }

    public String edit(Event event) {
        Event updatedEvent = eventService.updateEvent(event); // Update the detached object
        if (updatedEvent == null) {
            // Handle update failure
            return "admin?error=error saving event";
        }
        return "admin";
    }

    // edit by organizer
    public String editFromOrganizer(Event event) {
        if (file != null) {
            String fileName = String.valueOf(System.currentTimeMillis()) + "_" + Paths.get(file.getSubmittedFileName()).getFileName().toString();
            String directoryPath = "/home/trippleee/IdeaProjects/jee-project/src/main/webapp/resources/uploaded"; // Adjust this to your directory path
            try {
                // Save the uploaded file to the specified directory
                Files.copy(file.getInputStream(), Paths.get(directoryPath, fileName));

                // Set the imagePath property of the event to the path of the uploaded file
                String relativeImagePath = "/demo-1.0-SNAPSHOT/resources/uploaded/" + fileName;
                event.setImagePath(relativeImagePath);


                // Save the event and handle success or failure
                eventService.updateEvent(event);
                return "organizer";

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        eventService.updateEvent(event);

        return "organizer?error";
    }

    // delete the event
    public String delete(Long id){
        eventService.deleteEvent(id);
        return "admin";
    }

    public List<Event> allEvents(){
        return eventService.getAll();
    }

    // get all the events with their organizers
    public List<Event> allEventsWithOrganizers(){
        return eventService.getAllWithOrganizers();
    }

    public List<Event> getEventsByOrganizer(User organizer) {
        return eventService.getEventsByOrganizer(organizer);
    }

    // Return events as JSON string
    public String allEventsJson() {
        List<Event> events = this.allEventsWithOrganizers();
        JSONArray jsonArray = new JSONArray();

        for (Event event : events) {
            JSONObject eventJson = new JSONObject();
            eventJson.put("id", event.getId());
            eventJson.put("title", event.getTitle());
            eventJson.put("description", (event.getDescription()));
            eventJson.put("start", event.getStartDate().toString());
            eventJson.put("end", event.getEndDate().toString());
            eventJson.put("location", event.getLocation());
            eventJson.put("backgroundColor", event.getColor());
            eventJson.put("type", event.getType());
            eventJson.put("image", event.getImagePath());

            // Add organizers
            JSONArray organizersArray = new JSONArray();
            Set<User> organizers = event.getOrganizers();
            for (User organizer : organizers) {
                JSONObject organizerJson = new JSONObject();
                organizerJson.put("id", organizer.getId());
                organizerJson.put("name", organizer.getName());
                organizerJson.put("email", organizer.getEmail());
                organizersArray.put(organizerJson);
            }
            eventJson.put("organizers", organizersArray);

            jsonArray.put(eventJson);
        }
        return jsonArray.toString();
    }

    // save and remove event to participated list
    public String addToParticiped() {
        if (participedEventId == null || authMB.getLoggedUser().getId() == null) return "events";

        // check if the event is already saved if yes, remove it,
        // if not add it.
        if(this.isEventInParticipated(authMB.getLoggedUser(), Long.parseLong(participedEventId))){
            eventService.removeEventFromParticipates(Long.parseLong(participedEventId), authMB.getLoggedUser().getId());
            return "events";
        }
        eventService.addEventToParticipate(Long.parseLong(participedEventId), authMB.getLoggedUser().getId());
        return "events";
    }

    public String removeFromParticiped(Long eventId) {

        eventService.removeEventFromParticipates(eventId, authMB.getLoggedUser().getId());
        return "events";
    }

    // check if an event is in the list of participated of the user
    public boolean isEventInParticipated(User user, Long eventId) {
        List<Event> events = userMB.getParticipatedEvents(user);
        for (Event event : events) {
            if (event.getId().equals(eventId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEventToday (List<Event> events)
    {
        Date now = new Date();
        for (Event event : events) {
            if (event.getEndDate().after(now) && event.getStartDate().before(now)) return true;
        }
        return false;
    }

}
