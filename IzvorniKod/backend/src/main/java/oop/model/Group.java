package oop.model;

import jakarta.persistence.*;
import oop.service.GroupService;

import java.util.*;


@Entity
@Table(name = "SGroup")
public class Group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGrupa;

    private String nazivGrupa;

    private String uidPredstavnika;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Group(String nazivGrupa, String uidPredstavnika, Set<User> users, Set<Event> events, List<Message> messages) {
        this.nazivGrupa = nazivGrupa;
        this.uidPredstavnika = uidPredstavnika;
        this.users = users;
        this.events = events;
        this.messages = messages;
    }

    public Group() {}

    public int getIdGrupa() {
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

    public void addUser(User user) {
        users.add(user);
        user.setGroup(this);
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event){
        events.add(event);
        event.setGroup(this);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
        message.setGroup(this);
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
