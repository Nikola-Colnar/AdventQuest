package oop.service;

import oop.model.Event;
import oop.model.Message;
import oop.repository.EventRepository;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        //Provjera postoji li event s tim id
        if(eventRepository.findById(event.getIdEvent()).isPresent()) {
            throw new RuntimeException("User with this UID already exists");
        }
        return eventRepository.save(event);
    }

    public void deleteEvent(Event event) {
        if(!eventRepository.findById(event.getIdEvent()).isPresent()) {
            throw new RuntimeException("Event with this ID does not exist");
        }
        eventRepository.deleteById(event.getIdEvent());
    }
    public void ChangeEventName(int id, String newName) {
        Optional<Event> event = eventRepository.findById(id);
        if(event.isPresent()) {
            Event newEvent = event.get();
            newEvent.setEventName(newName);
            eventRepository.save(newEvent);
        } else {
            throw new NoSuchElementException("Event with this ID does not exist");
        }
    }
    public Event GetEventById(int id){
        Optional<Event> event = eventRepository.findById(id);
        if(!event.isPresent()) {
            throw new NoSuchElementException("Event with this ID does not exist");
        }
        return event.get();
    }
}
