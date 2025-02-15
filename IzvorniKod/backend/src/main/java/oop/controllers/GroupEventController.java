package oop.controllers;

import jakarta.servlet.http.HttpServletRequest;
import oop.model.*;
import oop.repository.RatedEventRepository;
import oop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oop.dto.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
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
    private final JWTService jwtService;

    @Autowired
    public GroupEventController(GroupService groupService,
                                UserService userService,
                                EventService eventService,
                                MessageService messageService,
                                RatedEventRepository ratedEventRepository,
                                JWTService jwtService) {
        this.groupService = groupService;
        this.userService = userService;
        this.eventService = eventService;
        this.messageService = messageService;
        this.ratedEventRepository = ratedEventRepository;
        this.jwtService = jwtService;
    }


    @GetMapping("/{groupId}/getIdPredstavnik") //Dohvaćanje ID Predstavnika grupe
    public ResponseEntity<Integer> getPredstavniksIdByGroupId(@PathVariable int groupId){
        Optional<Group> group = groupService.findById(groupId);
        if(group.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(group.get().getidPredstavnika());
    }

    @PostMapping("/{groupid}/addEvent")
    public ResponseEntity<EventDTO> createEvent(@PathVariable int groupid, @RequestBody Event event) {
        System.out.println("EVENT: +  " + event);
        Optional<Group> group = groupService.findById(groupid);

        if(group.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        event.setGroup(group.get());
        Event savedEvent = eventService.saveEvent(event);

        return ResponseEntity.ok(new EventDTO(savedEvent));
    }

    @GetMapping("/{groupId}/getEvents")
    public ResponseEntity<List<EventDTO>> getEventsByGroupId(@PathVariable int groupId) {
        Optional<Group> group = groupService.findById(groupId);

        if(group.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<EventDTO> eventsDTOS = groupService.getEventsByGroupId(groupId);
 

      

        eventsDTOS.sort(Comparator.comparing(EventDTO::getEventId));


        

        return ResponseEntity.ok(eventsDTOS);
    }

    @GetMapping("/{groupId}/getPastEvents") // vraća evenName, brojLajkova i username jel lajkao(da/ne)
    public ResponseEntity<List<IspisEventDTO>> getPastEventsByGroupId(@PathVariable int groupId, HttpServletRequest request) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
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
        // Provjera postoji li grupa
        Group group = groupService.findById(groupId).orElse(null);
        if (group == null) {
            // Ako grupa ne postoji, vraća se HTTP status 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Provjera postoji li event i je li povezan s grupom
        Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null || !group.getEvents().contains(event)) {
            // Ako event ne postoji ili nije povezan s grupom, vraća se HTTP status 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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

        Optional<Group> group = groupService.findById(groupId);

        if(group.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        message.setGroup(group.get());
        Message savedMessage = messageService.createMessage(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    @PostMapping("/{groupId}/deleteAIMessage/{msgID}") // brisanje AI poruke
    public ResponseEntity<Integer> deleteMessage(@PathVariable int groupId, @PathVariable int msgID) {
        System.out.println("brisanje AI poruke");
        Optional<Message> message = messageService.getMessageById(msgID);
        if(message.isEmpty()) {
            System.out.println("AI poruka s danim ID ne postoji");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User sender = userService.getUserById(Integer.parseInt(message.get().getIdSender())).get();
        if("chatBot".equals(sender.getUsername())) {
            messageService.deleteMessage(msgID);
            return ResponseEntity.ok(msgID);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{groupId}/getMessages") // dohvat nove poruka
    public ResponseEntity<List<MessageDTO>> getMessagesByGroupId(@PathVariable int groupId) {

        Optional<Group> group = groupService.findById(groupId);

        if(group.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(groupService.getAllMessages(groupId));
    }

    @PostMapping("/reviewEvent/{eventId}") // lajkanje eventa
    public ResponseEntity<RatedEventDTO> reviewEvent(HttpServletRequest request, @PathVariable int eventId) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        User user = userService.getUserByUsername(username);
        Optional<Event> event = eventService.getEventById(eventId);

        if(event.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        RatedEvent ratedEvent = new RatedEvent(1, user, event.get());
        eventService.saveRatedEvent(ratedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RatedEventDTO(user.getUsername(), event.get().getEventName(), 1));
    }

    @DeleteMapping("/deleteLike/{eventId}") // brisanje lajka
    public ResponseEntity<Integer> deleteLikeEvent(HttpServletRequest request, @PathVariable int eventId){
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        User user = userService.getUserByUsername(username);
        Optional<Event> event = eventService.getEventById(eventId);

        if(event.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int id = eventService.getRatedEventId(user.getId(), event.get().getIdEvent());

        user.getRatedEvents().remove(eventService.findRatedEventById(id));
        event.get().getRatedEvents().remove(eventService.findRatedEventById(id));

        eventService.deleteRatedEvenById(id);

        return ResponseEntity.ok(id);
    }

    @PostMapping("/addComment/{eventId}")
    public ResponseEntity<EventCommentDTO> addComment(HttpServletRequest request, @PathVariable int eventId,
                                                    @RequestBody String comment) {
        String username = jwtService.getUsernameFromHttpServletRequest(request);
        User user = userService.getUserByUsername(username);
        Optional<Event> event = eventService.getEventById(eventId);

        if(event.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        EventComments eventComments = new EventComments(user, event.get(), comment);
        eventService.addComment(eventComments);

        return ResponseEntity.status(HttpStatus.CREATED).body(new EventCommentDTO(
                username, event.get().getEventName(), comment, eventComments.getCommentId(), eventComments.getDate()));
    }

    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable int commentId) {
        eventService.deleteComment(commentId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/allComments/{eventId}")
    public ResponseEntity<List<EventCommentDTO>> getAllCommentsForEvent( @PathVariable int eventId) {
        Optional<Event> event = eventService.getEventById(eventId);
        if(event.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(eventService.getAllCommentsForEvent(eventId));
    }

    @GetMapping("/getEventProposals") // vraća 5 random prijedloga za event
    public ResponseEntity<List<String>> getEventProposals() {
        return ResponseEntity.ok(eventService.getFiveRandomEvents());
    }

    @PutMapping("/{groupId}/updateEvent/{eventId}") // mogućnost mijanja naziva, opisa i/ili boje eventa
    public ResponseEntity<EventDTO> updateEvent(@PathVariable int groupId, @PathVariable int eventId, @RequestBody Event event) {
        Optional<Event> e = eventService.getEventById(eventId);
        Optional<Group> group = groupService.findById(groupId);

        if(e.isEmpty() || group.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(!groupService.eventInGroup(groupId, eventId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(eventService.updateEvent(e.get(), event));

    }

    @PutMapping("/{groupId}/setDate/{eventId}") // postavljanje datuma
    public ResponseEntity<EventDTO> setEventDate(@PathVariable int groupId, @PathVariable int eventId, @RequestBody Event event){
        Optional<Event> e = eventService.getEventById(eventId);
        Optional<Group> group = groupService.findById(groupId);

        if(e.isEmpty() || group.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(!groupService.eventInGroup(groupId, eventId))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(eventService.updateDateEvent(e.get(), event));
    }
}