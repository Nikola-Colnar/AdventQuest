package oop.controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oop.model.Group;
import oop.model.User;
import oop.service.GroupService;
import oop.service.JWTService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController  //Rad izmedu usera i grupe
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired

    public UserController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    // Kreiranje nove grupe
    @PostMapping("/{userId}/createGroup")
    public ResponseEntity<Group> createGroup(@RequestBody Group group, @PathVariable String userId) {
        Group createdGroup = groupService.createGroup(group);
        User user = userService.getUserById(Integer.parseInt(userId)).get();
        user.getGroups().add(createdGroup);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{userId}/groups") //Dohvaćanje svih grupa od korisnika
    public ResponseEntity<List<String>> getGroupsByUserId(@PathVariable int userId) {
        List<String> listaGroupId = userService.getGroupsByUserId(userId);
        if (listaGroupId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(listaGroupId);
    }

    @GetMapping("/{groupId}/getUsers") //Dohvaćanje svih korisnika od grupe
    public Set<User> getAllUsers(@PathVariable int groupId) {
        Group group = groupService.getGroupById(groupId);
        return  group.getUsers();
    }

    @PostMapping("/{groupId}/addUser")
    public ResponseEntity<?> addUserToGroup(@PathVariable int groupId, @RequestBody Map<String, String> request) {
        try {
            // Extracting username from request
            String userName = request.get("username");
            if (userName == null || userName.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
            }

            // Fetching group and user
            Group group = groupService.getGroupById(groupId);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }

            User user = userService.getUserByUsername(userName);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Adding group to the user's groups (owning side)
            user.getGroups().add(group);
            userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

};
