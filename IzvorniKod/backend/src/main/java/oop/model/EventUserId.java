package oop.model;

import java.io.Serializable;
import java.util.Objects;

public class EventUserId implements Serializable {
    private int eventId;
    private String userId;

    // Default constructor
    public EventUserId() {}

    public EventUserId(int eventId, String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventUserId)) return false;
        EventUserId that = (EventUserId) obj;
        return eventId == that.eventId && userId == that.userId;
    }
}