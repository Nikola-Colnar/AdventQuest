package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //Rad izmedu grupa i događaja
@CrossOrigin("*")
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }




    @PostMapping("/{groupId}/addEvent") //Dodavanje događaja u grupu
    public ResponseEntity<Event> createEventForGroup(@PathVariable int groupId, @RequestBody Event event, @RequestHeader("id") String idUser) {
        System.out.println("Received ID USER: " + idUser);
        //Provjera postoji li grupa
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        group.addEvent(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
    @GetMapping("/{groupId}/getEvents") //Dohvaćanje svih događaja od grupe
    public ResponseEntity<List<Event>> getEventsByGroupId(@PathVariable int groupId) {
        List<Event> events = groupService.getEventsByGroupId(groupId);
        if (events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(events);
    }

}