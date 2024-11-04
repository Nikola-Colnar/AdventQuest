package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity //jako bitno da JPA prepozna
@Table(name = "users")  // stvara tablicu users
public class User {

    @Id   //oznacava id, ova dva @ su bitna
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
    @NotEmpty(message = "email is required")
    private String email;
    private String vrstaUser;

    // Default constructor
    public User() {
    }
    public User(String username, String password, String email, String vrstaUser){
        this.username = username;
        this.password = password;
        this.email = email;
        this.vrstaUser = vrstaUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "username is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "username is required") String username) {
        this.username = username;
    }

    public @NotEmpty(message = "password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "password is required") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "email is required") String email) {
        this.email = email;
    }

    public String getVrstaUser() {
        return vrstaUser;
    }

    public void setVrstaUser(String vrstaUser) {
        this.vrstaUser = vrstaUser;
    }

    @Override
    public String toString() {
        return username + " " + password + " " + email + " " + vrstaUser;
    }
}
