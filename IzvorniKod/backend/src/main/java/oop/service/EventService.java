package oop.service;

import oop.model.Event;
import oop.model.EventProposals;
import oop.repository.EventProposalsRepository;
import oop.repository.EventRepository;
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

    private final EventProposalsRepository eventProposalsRepository;

    public EventService(EventRepository eventRepository, EventProposalsRepository eventProposalsRepository) {
        this.eventRepository = eventRepository;
        this.eventProposalsRepository = eventProposalsRepository;
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
}