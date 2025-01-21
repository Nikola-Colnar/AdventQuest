package oop.service;

import oop.dto.EventCommentDTO;
import oop.dto.EventDTO;
import oop.model.*;
import oop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class EventService {

    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final EventProposalsRepository eventProposalsRepository;
    @Autowired
    private final RatedEventRepository ratedEventRepository ;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EventCommentsRepository eventCommentsRepository;


    public EventService(EventRepository eventRepository, EventProposalsRepository eventProposalsRepository, RatedEventRepository ratedEventRepository, UserRepository userRepository, EventCommentsRepository eventCommentsRepository) {
        this.eventRepository = eventRepository;
        this.eventProposalsRepository = eventProposalsRepository;
        this.ratedEventRepository = ratedEventRepository;
        this.userRepository = userRepository;
        this.eventCommentsRepository = eventCommentsRepository;
    }

    // Dohvati sve događaje
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Dohvati događaj po ID-u
    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    // Spremi novi događaj ili ažuriraj postojeći
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Izbriši događaj po ID-u
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }

    // Promijeni naziv događaja
    public void changeEventName(int id, String newName) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEventName(newName);
            eventRepository.save(event);
        } else {
            throw new RuntimeException("Event with this ID does not exist");
        }
    }

    public void deleteEventById(int id) {
        eventRepository.deleteById(id);
    }

    public void save(Event event) { eventRepository.save(event);}

    public EventProposals saveEventProposals(EventProposals eventProposals) {
        return eventProposalsRepository.save(eventProposals);
    }

    public List<String> getFiveRandomEvents() {
       List<EventProposals> eventProposalsList = new ArrayList<>(eventProposalsRepository.findAll());
       while(eventProposalsList.size() > 5){
           Random random = new Random();
           int r = random.nextInt(eventProposalsList.size());
           eventProposalsList.remove(r);
        }
       List<String> fiveRandomEvents = new ArrayList<>();
       for(EventProposals eventProposals : eventProposalsList){
           fiveRandomEvents.add(eventProposals.getTitle());
       }
       return fiveRandomEvents;
    }

    public RatedEvent saveRatedEvent(RatedEvent ratedEvent) {
        return ratedEventRepository.save(ratedEvent);
    }

    public int getRatedEventId(int userId, int eventId) {
        User user = userRepository.findById(userId).get();
        for(RatedEvent ratedEvent : user.getRatedEvents()){
            if(ratedEvent.getEvent().getIdEvent() == eventId){
                return ratedEvent.getRatedEventid();
            }
        }
        return 0;
    }

    public void deleteRatedEvenById(int ratedEventId){
        ratedEventRepository.deleteById(ratedEventId);
    }

    public RatedEvent findRatedEventById(int id) {
        return ratedEventRepository.findById(id).get();
    }

    public EventComments addComment(EventComments eventComments) {
        return eventCommentsRepository.save(eventComments);
    }

    public void deleteComment(int commentId) {
        eventCommentsRepository.deleteById(commentId);
    }

    public List<EventCommentDTO> getAllCommentsForEvent(int eventId) {
        List<EventCommentDTO> eventCommentDTOList = new ArrayList<>();
        for(EventComments eventComments : eventRepository.findById(eventId).get().getComments()){ // znam da postoji
            eventCommentDTOList.add(new EventCommentDTO(eventComments));
        }
        return eventCommentDTOList;
    }

    public EventDTO updateEvent(Event oldEvent, Event newEvent) {

        if(newEvent.getEventName() != null){ oldEvent.setEventName(newEvent.getEventName()); }
        if(newEvent.getDescription() != null){ oldEvent.setDescription(newEvent.getDescription()); }
        System.out.println(oldEvent.getColor());
        System.out.println(newEvent.getColor());
        if(newEvent.getColor() != null){ oldEvent.setColor(newEvent.getColor()); }
        System.out.println(oldEvent.getColor());
        eventRepository.save(oldEvent);

        return new EventDTO(oldEvent);
    }

    public EventDTO updateDateEvent(Event oldEvent, Event newEvent) {
        oldEvent.setDate(newEvent.getDate());
        eventRepository.save(oldEvent);
        return new EventDTO(oldEvent);
    }
}