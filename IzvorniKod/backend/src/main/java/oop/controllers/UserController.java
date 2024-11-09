package oop.controllers;

import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


//PROMIJERNI PORT AKO TREBA

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //ovaj endpoint koristiti za admine!!!
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Spremanje korisnika
        User createdUser = userService.createUser(user);

        // Vraća korisnika u odgovoru sa statusom 201 (Created)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //ovaj takodjer
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Dohvaća sve korisnike
        List<User> users = userService.getAllUsers();

        // Vraća listu korisnika sa statusom 200 (OK)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    //Endpoint za login, to ce biti na login formi(React)
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User loginDetails) {
        try {
            User user = userService.loginUser(loginDetails.getUsername(), loginDetails.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    //endpoint za sign up, iz forme za registraciju(React)
    @PostMapping("/signup")
    public ResponseEntity<User> signupUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }
}
