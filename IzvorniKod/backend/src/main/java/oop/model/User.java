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

    private String vrstaUser;

    @ManyToOne
    @JoinColumn(name = "idGrupa")
    private Group group;
    
    // Default constructor
    public User() {
    }

    // Konstruktor s UID, username i vrstaUser
    public User(String uid, String username, String vrstaUser) {
        this.uid = uid;
        this.username = username;
        this.vrstaUser = vrstaUser;
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

    public String getVrstaUser() {
        return vrstaUser;
    }

    public void setVrstaUser(String vrstaUser) {
        this.vrstaUser = vrstaUser;
    }

    @Override
    public String toString() {
        return uid + " " + username + " " +   vrstaUser;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}