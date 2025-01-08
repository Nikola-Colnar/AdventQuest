package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins="*")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // Kreiranje nove grupe
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }
    @PostMapping("/{groupId}/events")
    public ResponseEntity<Event> createEventForGroup(@PathVariable int groupId, @RequestBody Event event, @RequestHeader("uid") String uid) {
        System.out.println("Received UID: " + uid);
        //Provjera postoji li grupa
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        //samo predstavnik moze raditi grupe
        //Ako uidPredstavnika u grupi ne odgovara uid korisnika koji šalje zahtjev, status 403 (Forbidden) se vraća
        //Provjera da li je korisnik koji šalje zahtjev predstavnik (isti uid kao u grupi)
        if (!group.getUidPredstavnika().equals(uid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // Korisnik nije predstavnik ove grupe
        }

        // Postavljanje grupe za događaj
        event.setGroup(group);

        // Spremanje događaja s grupom
        groupService.putEvent(groupId, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
    @GetMapping("/{groupId}/events")
    public ResponseEntity<List<Event>> getEventsByGroupId(@PathVariable int groupId) {
        List<Event> events = groupService.getEventsByGroupId(groupId);
        if (events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(events);
    }

}