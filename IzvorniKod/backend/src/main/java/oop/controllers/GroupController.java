package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.model.User;
import oop.service.GroupService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //Rad izmedu grupa i događaja
@CrossOrigin("*")
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }




    @PostMapping("/{groupId}/addEvent") //Dodavanje događaja u grupu
    public ResponseEntity<Event> createEventForGroup(@PathVariable int groupId, @RequestBody Event event, @RequestHeader("idUser") String idUser) {
        System.out.println("Received ID USER: " + idUser);

        //Provjera postoji li grupa
        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera postoji li user
        Optional<User> user = userService.getUserById(Integer.parseInt(idUser));
        if(user.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera je li user predstavnik
        if(user.get().getId() != group.getidPredstavnika()){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);}


        group.addEvent(event);
        groupService.saveGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }



    @GetMapping("/{groupId}/getEvents") //Dohvaćanje svih događaja od grupe
    public ResponseEntity<List<Event>> getEventsByGroupId(@PathVariable int groupId) {

        //Provjera postoji li grupa
        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        List<Event> events = groupService.getEventsByGroupId(groupId);
        if (events.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    public ResponseEntity<Event> deleteEventForGroup(@PathVariable int groupId, @RequestParam int eventId, @RequestHeader("idUser") String idUser){

        //Provjera postoji li grupa
        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera postoji li user
        Optional<User> user = userService.getUserById(Integer.parseInt(idUser));
        if(user.isEmpty()){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera je li user predstavnik
        if(user.get().getId() != group.getidPredstavnika()){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);}

        group.deleteEventById(eventId);
        groupService.saveGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(group.getEventById(eventId));
    }

}