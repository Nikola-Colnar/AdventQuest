package oop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EventUsers")
public class EventUsers {

    @EmbeddedId  // Kombinirani primarni ključ koji povezuje Event i User
    private EventUserId id;



    // Default konstruktor
    public EventUsers() {
    }

    // Konstruktor za stvaranje nove instance
    public EventUsers(Event event, User user) {
        this.id = new EventUserId(event.getId(), user.getUid());  // Stvara kombinirani ID
    }

    // Getteri i setteri
    public EventUserId getId() {
        return id;
    }

    public void setId(EventUserId id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "EventUsers{" +
                "id=" + id +
                '}';
    }
}