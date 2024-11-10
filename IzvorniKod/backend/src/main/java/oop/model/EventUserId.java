package oop.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventUserId implements Serializable {
    private int eventId;
    private String userUid;

    // Default constructor
    public EventUserId() {}

    public EventUserId(int eventId, String userId) {
        this.eventId = eventId;
        this.userUid = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userUid;
    }

    public void setUserId(String userId) {
        this.userUid = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventUserId)) return false;
        EventUserId that = (EventUserId) obj;
        return eventId == that.eventId && userUid == that.userUid;
    }

    @Override
    public String toString() {
        return "EventUserId{" +
                "eventId=" + eventId +
                ", userUid=" + userUid +
                '}';
    }
}