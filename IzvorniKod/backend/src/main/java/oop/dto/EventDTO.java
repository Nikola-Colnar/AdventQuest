package oop.dto;

import oop.model.Event;

import java.time.LocalDate;

public class EventDTO {
    private final int eventId;
    private String eventName;
    private LocalDate date;
    private String description;
    private String color;


    // Konstruktor za popunjavanje DTO-a
    public EventDTO(Event event) {
        this.eventId = event.getIdEvent();
        this.eventName = event.getEventName();
        this.date = event.getDate();
        this.description = event.getDescription();
        this.color = event.getColor();
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

