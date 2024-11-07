package oop.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventUserId implements Serializable {
    private int eventId;
    private int userId;

    // Default constructor
    public EventUserId() {}

    public EventUserId(int eventId, int userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventUserId)) return false;
        EventUserId that = (EventUserId) obj;
        return eventId == that.eventId && userId == that.userId;
    }

    @Override
    public String toString() {
        return "EventUserId{" +
                "eventId=" + eventId +
                ", userId=" + userId +
                '}';
    }
}