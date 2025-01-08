package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotEmpty(message = "username is required")
    private String username;
    //password
    private String password;

    @Column(unique = true)
    @NotEmpty(message = "email is required")
    private  String email;


    

    @ManyToOne
    @JoinColumn(name = "idGrupa")
    private Group group;
    
    // Default constructor
    public User() {
    }

    // Konstruktor s password, username i vrstaUser
    public User(String username, String password, String email) {
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public @NotEmpty(message = "email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "email is required") String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return password + " " + username + " " + email;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}