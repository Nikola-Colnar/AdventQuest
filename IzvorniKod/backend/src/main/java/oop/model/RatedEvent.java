package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "RatedEvents")
public class RatedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratedEventid;

    private String review; // like or dislike
    private String description;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;

    public RatedEvent(User user, Event event, String review, String description) {
        this.review = review;
        this.description = description;
    }

    public RatedEvent() {}

    public int getRatedEventid() {return ratedEventid;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Event getEvent() {return event;}

    public void setEvent(Event event) {this.event = event;}

    public String getReview() {return review;}

    public void setReview(String review) {this.review = review;}

    public String getDescription() {return description;}

    public void setDescription(String description) {
        this.description = description;}

    public String getRatedEventname(){return event.getEventName();}
}
