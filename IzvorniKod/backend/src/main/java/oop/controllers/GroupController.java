package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.model.Message;
import oop.model.User;
import oop.service.EventService;
import oop.service.GroupService;
import oop.service.MessageService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController //Rad izmedu grupa i događaja
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final UserService userService;
    private final EventService eventService;
    private final MessageService messageService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService, EventService eventService, MessageService messageService) {
        this.groupService = groupService;
        this.userService = userService;
        this.eventService = eventService;
        this.messageService = messageService;
    }


    @PostMapping("/{groupId}/events") // stvaranje eventa u grupi
    public ResponseEntity<Event> createEventForGroup(@PathVariable int groupId, @RequestBody Event event,
                                                     @RequestHeader("uid") String uid) {
        // Postoji li grupa
        Optional<Group> group = groupService.findById(groupId);
        if (group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            event.setGroup(group.get());
            eventService.save(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(event);
        }
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

    @GetMapping("/{groupId}/deleteEvent") // brisanje eventa iz grupe
    public ResponseEntity<Set<Event>> deleteEventForGroup(@PathVariable int groupId, @RequestParam int eventId, @RequestHeader("idUser") String idUser){

        //Provjera postoji li grupa i user
        Group group = groupService.getGroupById(groupId);
        Optional<User> user = userService.getUserById(Integer.parseInt(idUser));
        if (group == null || user.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera je li user predstavnik
        if(user.get().getId() != group.getidPredstavnika()){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);}

        eventService.deleteEventById(eventId);
        groupService.saveGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(group.getEvents());
    }

    @PostMapping("/{groupId}/message") // dodavanje nove poruke
    public ResponseEntity<Message> createMessageForGroup(@PathVariable int groupId, @RequestBody Message message) {

        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        message.setGroup(group);
        Message savedMessage = messageService.createMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @GetMapping("/{groupId}/getMessage") // dohvat nove poruke
    public ResponseEntity<List<Message>> getMessagesByGroupId(@PathVariable int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}
        return ResponseEntity.status(HttpStatus.CREATED).body(group.getMessages());
    }


}