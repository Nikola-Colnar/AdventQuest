package oop.dto;

import oop.model.RatedEvent;

public class RatedEventDTO {

    private String username;
    private RatedEvent ratedEvent;
    private String review;
    private String description;
    private String eventName;

    public RatedEventDTO(String username, RatedEvent ratedEvent, String review, String description) {
        this.username = username;
        this.ratedEvent = ratedEvent;
        this.review = review;
        this.description = description;
    }

    public RatedEventDTO(String username, String eventName, String review, String description) {
        this.username = username;
        this.eventName = eventName;
        this.description = description;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public RatedEvent getRatedEvent() {
        return ratedEvent;
    }

    public String getReview() {
        return review;
    }

    public String getDescription() {
        return description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "RatedEventDTO{" +
                "username='" + username + '\'' +
                ", ratedEvent=" + ratedEvent +
                ", review='" + review + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
