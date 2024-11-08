package oop.controllers;

import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;


@CrossOrigin(origins="http://localhost:5174/")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        // Spremanje korisnika
//        User createdUser = userService.createUser(user);
//
//        // Vraća korisnika u odgovoru sa statusom 201 (Created)
//        return new ResponseEntity(createdUser, HttpStatus.CREATED);
//    }
//
//    public ResponseEntity<List<User>> getAllUsers() {
//        // Dohvaća sve korisnike
//        List<User> users = userService.getAllUsers();
//
//        // Vraća listu korisnika sa statusom 200 (OK)
//        return new ResponseEntity(users, HttpStatus.OK);
//    }

    @PostMapping("/create")
    public String createUser(@RequestBody User user) throws ExecutionException, InterruptedException {
        return userService.createUser(user);
    }

    @GetMapping("/get")
    public User getUser(@RequestParam String document_id) throws ExecutionException, InterruptedException {
        return userService.getUserById(document_id);
    }

    @PutMapping("/update")
    public String updateUser(@RequestBody User student) throws ExecutionException, InterruptedException {
        return userService.updateUser(student);
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestBody String document_id) throws ExecutionException, InterruptedException {
        return userService.deleteUser(document_id);
    }
}
