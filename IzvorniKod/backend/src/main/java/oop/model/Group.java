package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import oop.service.GroupService;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@Entity
@Table(name = "SGroup")
public class Group {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGrupa;

    @Column(unique = true)
    @NotEmpty(message = "Naziv grupe je obavezan")
    private String nazivGrupa;

    @NotNull(message = "Id predstavnika je obavezan")
    private int idPredstavnika;

    @ManyToMany(mappedBy = "groups")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Group(String nazivGrupa, int idPredstavnika) {
        this.nazivGrupa = nazivGrupa;
        this.idPredstavnika = idPredstavnika;
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

    public int getidPredstavnika() {
        return idPredstavnika;
    }

    public void setidPredstavnika(int uidPredstavnika) {
        this.idPredstavnika = uidPredstavnika;
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
                ", idPredstavnika='" + idPredstavnika + '\'' +
                ", users=" + users +
                ", events=" + events +
                ", messages=" + messages +
                '}';
    }
}