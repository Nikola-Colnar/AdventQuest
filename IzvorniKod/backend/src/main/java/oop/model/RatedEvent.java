package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RatedEvents")
public class RatedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratedEventid;

    private String review; // like or dislike

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    public RatedEvent(User user, Event event, String review) {
        this.review = review;
    }

    public RatedEvent() {}

    public int getRatedEventid() {return ratedEventid;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Event getEvent() {return event;}

    public void setEvent(Event event) {this.event = event;}

    public String getReview() {return review;}

    public void setReview(String review) {this.review = review;}

    @Override
    public String toString() {
        return "RatedEvent{" +
                "ratedEventid=" + ratedEventid +
                ", review='" + review + '\'' +
                ", username=" + user.getUsername() +
                ", even name=" + event.getEventName() +
                '}';
    }
}
