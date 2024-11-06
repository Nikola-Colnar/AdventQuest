package oop.model;


import jakarta.persistence.*;

@Entity
@Table(name = "EventUsers")
public class EventUsers {

    @EmbeddedId  //ovo je pravi primary key ove nase tablice koja povezuje evente sa svim userima, samo sto se ne prikazuje
    private EventUserId id;

    @ManyToOne
    @MapsId("eventId") // Povezuje ID događaja
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @MapsId("userId") // Povezuje ID korisnika
    @JoinColumn(name = "user_id")
    private User user;

    // Default konstruktor
    public EventUsers() {
    }

    public EventUsers(Event event, User user) {
        this.event = event;
        this.user = user;
        this.id = new EventUserId(event.getId(), user.getId());
    }

    public EventUserId getId() {
        return id;
    }

    public void setId(EventUserId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
