package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RatedEvents")
public class RatedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratedEventid;

    private int userId;
    private int eventId;
    private String rewiev; // like or dislike

    public RatedEvent(int userId, int eventId, String rewiev) {
        this.userId = userId;
        this.eventId = eventId;
        this.rewiev = rewiev;
    }

    public RatedEvent() {}

    public Long getRatedEventid() {return ratedEventid;}

    public int getUserId() {return userId;}

    public void setUserId(int userId) {this.userId = userId;}

    public int getEventId() {return eventId;}

    public void setEventId(int eventId) {this.eventId = eventId;}

    public String getRewiev() {return rewiev;}

    public void setRewiev(String rewiev) {this.rewiev = rewiev;}

    @Override
    public String toString() {
        return "RatedEvent{" +
                "ratedEventid=" + ratedEventid +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", rewiev='" + rewiev + '\'' +
                '}';
    }
}
