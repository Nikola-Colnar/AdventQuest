package oop.controllers;

import oop.model.*;
import oop.repository.RatedEventRepository;
import oop.service.EventService;
import oop.service.GroupService;
import oop.service.MessageService;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oop.dto.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    private final RatedEventRepository ratedEventRepository;

    @Autowired
    public GroupEventController(GroupService groupService, UserService userService, EventService eventService, MessageService messageService, RatedEventRepository ratedEventRepository) {
        this.groupService = groupService;
        this.userService = userService;
        this.eventService = eventService;
        this.messageService = messageService;
        this.ratedEventRepository = ratedEventRepository;
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
    @GetMapping("/{groupId}/getIdPredstavnik") //Dohvaćanje ID Predstavnika grupe
    public ResponseEntity<Integer> getPredstavniksIdByGroupId(@PathVariable int groupId){

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        return ResponseEntity.ok(group.getidPredstavnika()); // zanemari žuto, sve radi
    }

    @PostMapping("/{groupid}/addEvent")
    public ResponseEntity<Event> createEvent(@PathVariable int groupid, @RequestBody Event event) {

        Group group = groupService.findById(groupid).orElseThrow(() -> new RuntimeException("Group not found"));
        event.setGroup(group);
        Event savedEvent = eventService.saveEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/{groupId}/getEvents")
    public ResponseEntity<List<EventDTO>> getEventsByGroupId(@PathVariable int groupId) {

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        List<EventDTO> events = group.getEvents().stream()
                .map(EventDTO::new) // Mapira svaki `Event` u `EventDTO`
                .collect(Collectors.toList());

        if (events.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{groupId}/deleteEvent/{eventId}") // brisanje eventa iz grupe
    public ResponseEntity<Set<Event>> deleteEventForGroup(@PathVariable int groupId, @PathVariable int eventId){
        //DODAJ AUTENTIFIKACIJU DA JE ZAHTJEV POSLAO PREDSTAVNIK TOKEN!!!
        //ZAKOMENTIRAN kod je stara logika treba se zamjeniti s sadasnjom autentifikacijom da radi isto!!
        //////////////////////////////////////////////////////
        //Provjera postoji li grupa i user
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        //Optional<User> user = userService.getUserById(Integer.parseInt(idUser));
        //if (group == null || user.isEmpty()) {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);}

        //provjera je li user predstavnik
        //if(user.get().getId() != group.getidPredstavnika()){return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);}

        eventService.deleteEventById(eventId);
        groupService.saveGroup(group);

        return ResponseEntity.status(HttpStatus.CREATED).body(group.getEvents());
    }

    @PostMapping("/{groupId}/addMessage") // dodavanje nove poruke
    public ResponseEntity<Message> createMessageForGroup(@PathVariable int groupId, @RequestBody Message message) {

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        message.setGroup(group);
        Message savedMessage = messageService.createMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @GetMapping("/{groupId}/getMessages") // dohvat nove poruka
    public ResponseEntity<List<Message>> getMessagesByGroupId(@PathVariable int groupId) {

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        return ResponseEntity.ok(group.getMessages());
    }

    @PostMapping("/{username}/reviewEvent/{eventId}") // dodavanje nove ocijene eventa
    public ResponseEntity<RatedEventDTO> reviewEvent(@PathVariable String username, @PathVariable int eventId, @RequestParam String review) {

        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        RatedEvent ratedEvent = new RatedEvent(user, event, review);
        ratedEventRepository.save(ratedEvent);
        user.addRatedEvent(ratedEvent);
        event.addRatedEvent(ratedEvent);
        eventService.saveEvent(event);
        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RatedEventDTO(ratedEvent.getRatedEventid(), user.getUsername(), event.getEventName(), review));
    }

    @GetMapping("/{username}/getUserReviews") // dohvaća sve eventove koje je user ocijenio
    public ResponseEntity<List<RatedEventDTO>> getUserReviews(@PathVariable String username) {

        User user = userService.getUserByUsername(username);
        List<RatedEventDTO> ratedEventsDTO = new ArrayList<>();

        for(RatedEvent ratedEvent : user.getRatedEvents()) { // pretvorba u DTO nez jel ima brži i bolji način
            ratedEventsDTO.add(new RatedEventDTO(ratedEvent.getRatedEventid(), ratedEvent.getUser().getUsername(),
                    ratedEvent.getEvent().getEventName(), ratedEvent.getReview()));
        }

        return ResponseEntity.ok(ratedEventsDTO);
    }

   @GetMapping("/{eventId}/getEventReviews") // lista usera koji su ocijenili neki event
    public ResponseEntity<List<UserDTO>> getEventReviews(@PathVariable int eventId) {

        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        List<UserDTO> userDTOs= new ArrayList<>();

        for(RatedEvent ratedEvent : event.getRatedEvents()) {
            userDTOs.add(new UserDTO(ratedEvent.getUser()));
        }

        return ResponseEntity.ok(userDTOs);
    }
}