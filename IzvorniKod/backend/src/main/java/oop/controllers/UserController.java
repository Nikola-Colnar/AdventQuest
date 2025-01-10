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
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{userId}/groups") //Dohvaćanje svih grupa od korisnika
    public ResponseEntity<Set<Group>> getGroupsByUserId(@PathVariable int id) {
        Set<Group> groups = userService.getGroupsByUserId(id);
        if (groups.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{groupId}/getUsers") //Dohvaćanje svih korisnika od grupe
    public Set<User> getAllUsers(@PathVariable int groupId) {
        Group group = groupService.getGroupById(groupId);
        return  group.getUsers();
    }

    @PostMapping("/{groupId}/addUser")
    public ResponseEntity<User> addUserToGroup(@PathVariable int groupId, @RequestBody String userJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userJson);
            String userName = jsonNode.get("username").asText();

            System.out.println("Received username: " + userName);
            Group group = groupService.getGroupById(groupId);
            User user = userService.getUserByUsername(userName);
            if (group == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            group.addUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

};
