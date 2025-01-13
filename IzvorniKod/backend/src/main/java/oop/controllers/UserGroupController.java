package oop.controllers;

import oop.model.Group;
import oop.model.User;
import oop.service.GroupService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController  //Rad izmedu usera i grupe
@CrossOrigin(origins = "http://localhost:5173")
public class UserGroupController {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired

    public UserGroupController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    // Kreiranje nove grupe
    @PostMapping("/{username}/createGroup")
    public ResponseEntity<Group> createGroup(@RequestBody Group group, @PathVariable String username) {
        User user = userService.getUserByUsername(username);

        // Postavljamo idPredstavnika na id korisnika
        group.setidPredstavnika(user.getId());  // Koristimo id korisnika umjesto username-a

        // Kreiramo grupu
        Group createdGroup = groupService.createGroup(group);

        // Povezujemo korisnika s grupom
        user.getGroups().add(createdGroup);
        userService.saveUser(user);  // Save the user with the new group

        // Dodajemo korisnika u grupu
        createdGroup.getUsers().add(user);
        groupService.saveGroup(createdGroup);  // Save the group with the new user

        // Vraćamo odgovor sa statusom 201 (Created) i novom grupom
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @GetMapping("/{username}/getGroups") //Dohvaćanje svih grupa od korisnika (pomocu usernamea) (Vraća groupname vrlo lako može i vraćati group Id)
    public ResponseEntity<List<Object>> getAllGroupsByUsername(@PathVariable String username) {
        // Dohvati korisnika prema username-u
        User user = userService.getUserByUsername(username);

        // Dohvati grupe prema ID-u korisnika
        List<Object> groupInfoList = userService.getGroupsByUserId(user.getId());

        if (groupInfoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        // Vraćamo listu objekata sa groupId i groupName
        return ResponseEntity.ok(groupInfoList);
    }


    @GetMapping("/{groupId}/getUsers") //Dohvaćanje svih korisnika od grupe (Vraća username vrlo lako može i vraćati user Id)
    public ResponseEntity<List<String>> getAllUsersByGroupId(@PathVariable int groupId) {  //ovo moze sa tokenima kasnije
        List<String> listaUserNames = userService.getAllUsersByGroupId(groupId);
        if (listaUserNames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(listaUserNames);
    }

    @PostMapping("/{groupId}/addUser") // dodavanje novih usera u grupu
    public ResponseEntity<?> addUserToGroup(@PathVariable int groupId, @RequestBody Map<String, String> request) { //ovo moze sa tokenima kasnije
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
            if(user.getId() != group.getidPredstavnika()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not a representative of the group");
            }
            // Adding group to the user's groups (owning side)
            user.getGroups().add(group);
            userService.saveUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/{username}/group/{groupId}")    // Brisanje korisnika iz grupe
    public boolean deleteUserFromGroup(@PathVariable String username, @PathVariable int groupId) {
        User user = userService.getUserByUsername(username);
        if (user == null) {return false;}

        Group group = groupService.getGroupById(groupId);
        if (group == null) {return false;}

        user.getGroups().remove(group);
        userService.saveUser(user);
        return true;
    }
};
