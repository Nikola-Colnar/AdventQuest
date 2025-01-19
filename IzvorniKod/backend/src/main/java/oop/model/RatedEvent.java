package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserLikedEvents")
public class RatedEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rated_event_id")
    private int ratedEventId;

    @Column(name = "liked", nullable = false)
    private int liked; // 1 ako je lajkao, inače 0

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    public RatedEvent(int liked) {
        this.liked = liked;
    }

    public RatedEvent(int liked, User user, Event event) {
        this.liked = liked;
        this.user = user;
        this.event = event;
    }

    public RatedEvent() {}


    public int getLiked() {return liked;}

    public void setLiked(int like) {this.liked = like;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Event getEvent() {return event;}

    public void setEvent(Event event) {this.event = event;}

    public int getRatedEventid() {return ratedEventId;}
}
