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

import java.sql.Date;
import java.time.LocalDate;
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

    @GetMapping("/{adminName}/getGroups")
    public ResponseEntity<List<GroupDTO>> getAllGroups(@PathVariable String adminName){
        //if(!Objects.equals(adminName, "Romeo")) Promijeni u kojeg kod usera
        //   return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(groupService.findAllGroups());
    }

    @GetMapping("/{adminName}/getUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PathVariable String adminName){
        //if(!Objects.equals(adminName, "Romeo")) Promijeni u kojeg kod usera
        //   return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(userService.findAllUsers());
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

    @GetMapping("/{groupId}/getPastEvents/{username}") // vraća evenName, brojLajkova i username jel lajkao(da/ne)
    public ResponseEntity<List<IspisEventDTO>> getPastEventsByGroupId(@PathVariable int groupId, @PathVariable String username) {

        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userService.getUserByUsername(username);

        List<Event> events = new ArrayList<>(group.getEvents()); // Eventovi u grupi
        List<IspisEventDTO> list = new ArrayList<>();
        //System.out.println(events.size());
        for(Event event : events){
           if(event.getDate() != null){
               int numOfLikes = event.getRatedEvents().size();
               int userLajkao = 0;
               System.out.println(numOfLikes);
               for(RatedEvent e: user.getRatedEvents()){ // provjera jeli user lajkao
                   if(e.getEvent().getIdEvent() == event.getIdEvent()){
                       userLajkao=1;
                       break;
                   }
               }
               list.add(new IspisEventDTO(event.getEventName(), numOfLikes, userLajkao));
           }
       }
       return ResponseEntity.ok(list);
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

    @PostMapping("/{username}/reviewEvent/{eventId}") // lajkanje eventa
    public ResponseEntity<RatedEventDTO> reviewEvent(@PathVariable String username, @PathVariable int eventId) {

        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        RatedEvent ratedEvent = new RatedEvent(1, user, event);
        eventService.saveRatedEvent(ratedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RatedEventDTO(user.getUsername(), event.getEventName(), 1));
    }

    @DeleteMapping("/{username}/deleteLike/{eventId}") // brisanje lajka
    public ResponseEntity<Integer> deleteLikeEvent(@PathVariable String username, @PathVariable int eventId){

        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        int id = eventService.getRatedEventId(user.getId(), event.getIdEvent());

        user.getRatedEvents().remove(eventService.findRatedEventById(id));
        event.getRatedEvents().remove(eventService.findRatedEventById(id));

        eventService.deleteRatedEvenById(id);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/{username}/addComment/{eventId}")
    public ResponseEntity<EventCommentDTO> addComment(@PathVariable String username, @PathVariable int eventId,
                                                    @RequestBody String comment) {
        User user = userService.getUserByUsername(username);
        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        EventComments eventComments = new EventComments(user, event, comment);
        eventService.addComment(eventComments);

        return ResponseEntity.status(HttpStatus.CREATED).body(new EventCommentDTO(
                username, event.getEventName(), comment, eventComments.getCommentId(), eventComments.getDate()));
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable int commentId) {
        eventService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/allComments/{eventId}")
    public ResponseEntity<List<EventCommentDTO>> getAllCommentsForEvent( @PathVariable int eventId) {
        Event event = eventService.getEventById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        List<EventCommentDTO> list = new ArrayList<>();
        for(EventComments ec: event.getComments()){
            list.add(new EventCommentDTO(ec));
        }
        return ResponseEntity.ok(list);
    }


    @GetMapping("/getEventProposals") // vraća 5 random prijedloga za event
    public ResponseEntity<List<String>> getEventProposals() {
        return ResponseEntity.ok(eventService.getFiveRandomEvents());
    }

    @PutMapping("/{groupId}/updateEvent/{eventId}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable int groupId, @PathVariable int eventId, @RequestBody Event event) {
        Event e = eventService.getEventById(eventId).orElseThrow(()-> new RuntimeException("Event not found"));
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        for(Event e2 : group.getEvents()){
            if(eventId == e2.getIdEvent()){ // event je u toj grupi
              if(event.getEventName() != null){ e.setEventName(event.getEventName()); }
              if(event.getDescription() != null){ e.setDescription(event.getDescription()); }
              if(event.getColor() != null){ e.setColor(event.getColor()); }
              eventService.saveEvent(e);
              return ResponseEntity.ok(new EventDTO(e));
            }
        }
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PutMapping("/{groupId}/setDate/{eventId}") // postavljanje datuma
    public ResponseEntity<EventDTO> setEventDate(@PathVariable int groupId, @PathVariable int eventId, @RequestBody Event event){
        Event e = eventService.getEventById(eventId).orElseThrow(()-> new RuntimeException("Event not found"));
        Group group = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        for(Event e2 : group.getEvents()){
            if(eventId == e2.getIdEvent()){ // event je u toj grupi
                e.setDate(event.getDate());
                eventService.saveEvent(e);
                return ResponseEntity.ok(new EventDTO(e));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}