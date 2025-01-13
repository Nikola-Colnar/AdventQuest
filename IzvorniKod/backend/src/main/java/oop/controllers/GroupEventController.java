package oop.controllers;

import oop.model.*;
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
import java.util.stream.Collectors;

@RestController //Rad izmedu grupa i događaja
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/groups")
public class GroupEventController {

    private final GroupService groupService;
    private final UserService userService;
    private final EventService eventService;
    private final MessageService messageService;

    @Autowired
    public GroupEventController(GroupService groupService, UserService userService, EventService eventService, MessageService messageService) {
        this.groupService = groupService;
        this.userService = userService;
        this.eventService = eventService;
        this.messageService = messageService;
    }


//    @PostMapping("/{groupId}/addEvent") // stvaranje eventa u grupi
//    public ResponseEntity<Event> createEventForGroup(@PathVariable int groupId, @RequestBody Event event) {
//        // Postoji li grupa
//        Optional<Group> group = groupService.findById(groupId);
//        if (group.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } else {
//            event.setGroup(group.get());
//            eventService.save(event);
//            return ResponseEntity.status(HttpStatus.CREATED).body(event);
//        }
//    }

    @PostMapping("/{groupid}/addEvent")
    public ResponseEntity<Event> createEvent(@PathVariable int groupid, @RequestBody Event event) {
        Optional<Group> group = Optional.ofNullable(groupService.getGroupById(groupid));

        if (group.isPresent()) {
            event.setGroup(group.get());
            Event savedEvent = eventService.saveEvent(event);
            return ResponseEntity.ok(savedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{groupId}/getEvents")
    public ResponseEntity<List<EventDTO>> getEventsByGroupId(@PathVariable int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        List<EventDTO> events = group.getEvents().stream()
                .map(EventDTO::new) // Mapira svaki `Event` u `EventDTO`
                .collect(Collectors.toList());

        if (events.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        return ResponseEntity.ok(events);
    }


    @PostMapping("/{groupId}/deleteEvent") // brisanje eventa iz grupe
    public ResponseEntity<Set<Event>> deleteEventForGroup(@PathVariable int groupId, @RequestParam int eventId,
                                                          @RequestHeader("idUser") String idUser){

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

    @PostMapping("/{groupId}/addMessage") // dodavanje nove poruke
    public ResponseEntity<Message> createMessageForGroup(@PathVariable int groupId, @RequestBody Message message) {

        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        message.setGroup(group);
        Message savedMessage = messageService.createMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @GetMapping("/{groupId}/getMessages") // dohvat nove poruka
    public ResponseEntity<List<Message>> getMessagesByGroupId(@PathVariable int groupId) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}
        return ResponseEntity.status(HttpStatus.CREATED).body(group.getMessages());
    }


}