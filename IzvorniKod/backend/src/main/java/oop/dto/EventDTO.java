package oop.dto;

import oop.model.Event;

public class EventDTO {
    private final int eventId;
    private String eventName;
    private String startDate;
    private String endDate;
    private String description;
    private String color;

    // Konstruktor za popunjavanje DTO-a
    public EventDTO(Event event) {
        this.eventId = event.getIdEvent();
        this.eventName = event.getEventName();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
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

