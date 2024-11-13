package oop.model;

import jakarta.persistence.*;
import java.util.*;


@Entity
@Table(name = "SGroup")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idGrupa;

    private String nazivGrupa;

    private String uidPredstavnika;

    @OneToMany
    private Set<User> users = new HashSet<>();

    @OneToMany
    private Set<Event> events = new HashSet<>();

    @OneToMany
    private List<Message> messages = new ArrayList<>();

    public Long getIdGrupa() {
        return idGrupa;
    }

    public String getNazivGrupa() {
        return nazivGrupa;
    }

    public void setNazivGrupa(String nazivGrupa) {
        this.nazivGrupa = nazivGrupa;
    }

    public String getUidPredstavnika() {
        return uidPredstavnika;
    }

    public void setUidPredstavnika(String uidPredstavnika) {
        this.uidPredstavnika = uidPredstavnika;
    }


    public Set<User> getUsers() {
        return users;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Group{" +
                "idGrupa=" + idGrupa +
                ", nazivGrupa='" + nazivGrupa + '\'' +
                ", uidPredstavnika='" + uidPredstavnika + '\'' +
                ", users=" + users +
                ", events=" + events +
                ", messages=" + messages +
                '}';
    }
}
