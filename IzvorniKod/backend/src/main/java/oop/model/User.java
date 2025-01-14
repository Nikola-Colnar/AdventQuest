package oop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotEmpty(message = "username is required")
    private String username;

    private String password;

    @Column(unique = true)
    @NotEmpty(message = "email is required")
    private  String email;

    @ManyToMany
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "idgrupa")
    )
    private Set<Group> groups = new HashSet<>();

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

    public void addGroup(Group group) {
        groups.add(group);
        group.getUsers().add(this); // dodavanje korisnika u grupu
    }

    public void removeGroup(Group group) {
        groups.remove(group);
        group.getUsers().remove(this); // Sinhronizacija relacije
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

}