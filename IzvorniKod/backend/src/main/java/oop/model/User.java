package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    //UID od FireBase
    @NotEmpty(message = "UID is required")


    private String uid;

    @NotEmpty(message = "username is required")
    private String username;


    @ManyToOne
    @JoinColumn(name = "idGrupa")
    private Group group;
    
    // Default constructor
    public User() {
    }

    // Konstruktor s UID, username i vrstaUser
    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return uid + " " + username + " ";
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}