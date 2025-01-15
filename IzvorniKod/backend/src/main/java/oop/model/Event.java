package oop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent;

    @JsonProperty("StartDate")
    private String startDate;

    @JsonProperty("EndDate")
    private String endDate;

    private String eventName;
    private String color = "green";
    private String description;

    @ManyToOne
    @JoinColumn(name = "idGrupa")
    private Group group;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RatedEvent> ratedEvents = new HashSet<>();

    public Event() {}

    public Event(String eventName, String startDate, String endDate, String description) {
        this(eventName, startDate, endDate, description, "green");
    }

    public Event(String eventName, String startDate, String endDate, String description, String color) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventName = eventName;
        this.description = description;
        this.color = color;
    }

    // Getters and setters

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<RatedEvent> getRatedEvents() {return ratedEvents;}

    public void addRatedEvent(RatedEvent ratedEvent) {
        ratedEvents.add(ratedEvent);
        ratedEvent.setEvent(this);
    }

    public void removeRatedEvent(RatedEvent ratedEvent) {
        ratedEvents.remove(ratedEvent);
        ratedEvent.setEvent(null);
    }

    @Override
    public String toString() {
        return "Event{" +
                "idEvent=" + idEvent +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", eventName='" + eventName + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", group=" + group +
                '}';
    }
}