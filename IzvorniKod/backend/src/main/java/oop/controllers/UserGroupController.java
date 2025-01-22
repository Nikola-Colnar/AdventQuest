package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.model.User;
import oop.repository.EventRepository;
import oop.service.EventService;
import oop.service.GroupService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import oop.dto.*;

@RestController  //Rad izmedu usera i grupe
@CrossOrigin(origins = "http://localhost:5173")
public class UserGroupController {

    private final UserService userService;
    private final GroupService groupService;
    private final EventService eventService;

    @Autowired

    public UserGroupController(UserService userService, GroupService groupService, EventService eventService) {
        this.userService = userService;
        this.groupService = groupService;
        this.eventService = eventService;
    }

    // Kreiranje nove grupe
    @PostMapping("/{username}/createGroup")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody Group group, @PathVariable String username) {
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
        GroupDTO groupDTO = new GroupDTO(createdGroup.getIdGrupa(), createdGroup.getNazivGrupa());

        // Vraćamo odgovor sa statusom 201 (Created) i novom grupom
        return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);
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

    @PostMapping("/{groupId}/addUser")
    public ResponseEntity<GroupDTO> addUserToGroup(@PathVariable int groupId, @RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            if (username == null || username.isEmpty()) {return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);}

            Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

            User user = userService.getUserByUsername(username);
            if (user == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

            user.addGroup(group);
            userService.saveUser(user);

            GroupDTO groupDTO = new GroupDTO(group.getIdGrupa(), group.getNazivGrupa());

            return ResponseEntity.status(HttpStatus.CREATED).body(groupDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{username}/getUserId") //Vraća id usera
    public ResponseEntity<Integer> getUserId(@PathVariable String username) {  //ovo moze sa tokenima kasnije
        return ResponseEntity.ok(userService.getUserByUsername(username).getId());
    }

    @DeleteMapping("/user/{username}/group/{groupId}")    // Brisanje korisnika iz grupe
    public boolean deleteUserFromGroup(@PathVariable String username, @PathVariable int groupId) {

        User user = userService.getUserByUsername(username);
        if (user == null) {return false;}

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        user.getGroups().remove(group);
        userService.saveUser(user);
        return true;
    }

    @GetMapping("/{adminName}/getAllGroups")
    public ResponseEntity<List<GroupDTO>> getAllGroups(@PathVariable String adminName){
        if(userService.getUserByUsername(adminName).getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(groupService.findAllGroups());
    }

    @GetMapping("/{adminName}/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PathVariable String adminName){
        if(userService.getUserByUsername(adminName).getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/{username}/addAdmin")
    public ResponseEntity<UserDTO> addAdmin(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userService.addAdmin(user));
    }

    @DeleteMapping("/admin/{username1}/deleteAdmin/{username2}")
    public ResponseEntity<Boolean> deleteAdmin(@PathVariable String username1, @PathVariable String username2) {
        User user1 = userService.getUserByUsername(username1);
        User user2 = userService.getUserByUsername(username2);
        if(user1.getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(user2.getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(userService.deleteUser(user2));
    }

    @DeleteMapping("/admin/{username}/deleteUser/{username2}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable String username, @PathVariable String username2) {
        if(userService.getUserByUsername(username).getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(userService.deleteUser(userService.getUserByUsername(username2)));
    }

    @GetMapping("/{username}/isAdmin")
    public ResponseEntity<Boolean> isAdmin(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user.getIsAdmin() == 1);
    }

    @GetMapping("/{username}/predstavnik/{groupId}")
    public ResponseEntity<Boolean> getPredstavnik(@PathVariable String username, @PathVariable int groupId) {
        User user = userService.getUserByUsername(username);
        Optional<Group> group = groupService.findById(groupId);
        if(group.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(user.getId() == group.get().getidPredstavnika());
    }

}