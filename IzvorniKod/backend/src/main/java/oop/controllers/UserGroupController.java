package oop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import oop.model.Event;
import oop.model.Group;
import oop.model.User;
import oop.repository.EventRepository;
import oop.service.EventService;
import oop.service.GroupService;
import oop.service.JWTService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
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
    private final JWTService jwtService;

    @Autowired

    public UserGroupController(UserService userService, GroupService groupService, EventService eventService, JWTService jwtService) {
        this.userService = userService;
        this.groupService = groupService;
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    // Kreiranje nove grupe
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody Group group, HttpServletRequest request) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
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

    @GetMapping("/getGroups") //Dohvaćanje svih grupa od korisnika (pomocu usernamea) (Vraća groupname vrlo lako može i vraćati group Id)
    public ResponseEntity<List<Object>> getAllGroupsByUsername(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
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
    public ResponseEntity<GroupDTO> addUserToGroup(@PathVariable int groupId, @RequestBody Map<String, String> request,
                                                   HttpServletRequest httpRequest) {
        String usernameKorisnika = jwtService.getUsernameFromHttpServletRequest(httpRequest);
        User userKorisnik = userService.getUserByUsername(usernameKorisnika);

        int idKorisnika = userKorisnik.getId();
        int idPredstavnika = groupService.findById(groupId).get().getidPredstavnika();
        try {
            String username = request.get("username");
            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

            User user = userService.getUserByUsername(username);
            if (user == null) {
                System.out.println("User not found: " + username);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            if (idKorisnika != idPredstavnika) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
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

    @GetMapping("/getUserId") //Vraća id usera
    public ResponseEntity<Integer> getUserId(HttpServletRequest request) {
        //ovo moze sa tokenima kasnije
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        return ResponseEntity.ok(userService.getUserByUsername(username).getId());
    }

    @DeleteMapping("/user/{username}/group/{groupId}")    // Brisanje korisnika iz grupe
    public ResponseEntity<Void> deleteUserFromGroup(@PathVariable String username, @PathVariable int groupId,
                                                    HttpServletRequest httpRequest) {
        String usernameKorisnika = jwtService.getUsernameFromHttpServletRequest(httpRequest);
        User userKorisnik = userService.getUserByUsername(usernameKorisnika);
        int idKorisnika = userKorisnik.getId();

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        if (idKorisnika != userKorisnik.getIsAdmin() && idKorisnika != group.getidPredstavnika()) {
            //provjera jeli korisnik predstanvik ili admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        user.getGroups().remove(group);
        userService.saveUser(user);
        return ResponseEntity.noContent().build();
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

    @DeleteMapping("/admin/deleteAdmin/{username2}") //brisanje admina
    public ResponseEntity<Boolean> deleteAdmin(HttpServletRequest request, @PathVariable String username2) {
        String username1 = jwtService.getUsernameFromHttpServletRequest(request);
        User user1 = userService.getUserByUsername(username1);
        User user2 = userService.getUserByUsername(username2);
        if(user1.getIsAdmin() != 1) //ako si loginan kao admin, koristis taj endpoint kako bi izbrisao admina
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if(user2.getIsAdmin() != 1)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(userService.deleteUser(user2));
    }

    @DeleteMapping("/admin/deleteUser/{username2}") //brisanje korisnika
    public ResponseEntity<Boolean> deleteUser(HttpServletRequest request, @PathVariable String username2) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        if(userService.getUserByUsername(username).getIsAdmin() != 1) //provjera jesi li loginan kao admin
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(userService.deleteUser(userService.getUserByUsername(username2)));
    }

    @GetMapping("/isAdmin")
    public ResponseEntity<Boolean> isAdmin(HttpServletRequest request) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user.getIsAdmin() == 1); //returna true ako je admin
    }

    @GetMapping("/{username}/predstavnik/{groupId}")  //provjera je li korisnik predstavnik grupe
    public ResponseEntity<Boolean> getPredstavnik(@PathVariable String username, @PathVariable int groupId) {
        User user = userService.getUserByUsername(username);
        Optional<Group> group = groupService.findById(groupId);
        if(group.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.ok(user.getId() == group.get().getidPredstavnika());
    }


}
