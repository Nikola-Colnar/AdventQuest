package oop.controllers;

import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//PROMIJERNI PORT AKO TREBA

@CrossOrigin(origins="http://localhost:5173/")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Spremanje korisnika
        User createdUser = userService.createUser(user);

        // Vraća korisnika u odgovoru sa statusom 201 (Created)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Dohvaća sve korisnike
        List<User> users = userService.getAllUsers();

        // Vraća listu korisnika sa statusom 200 (OK)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
