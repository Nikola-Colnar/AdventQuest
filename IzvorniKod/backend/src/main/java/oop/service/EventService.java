package oop.service;

import oop.model.Event;
import oop.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

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

    // Kreiraj događaj s provjerom dupliciranja
    public Event createEvent(Event event) {
        if (event.getIdEvent() != 0 && eventRepository.existsById(event.getIdEvent())) {
            throw new RuntimeException("Event with this ID already exists");
        }
        return eventRepository.save(event);
    }

    public void deleteEventById(int id) {
        eventRepository.deleteById(id);
    }

    public void save(Event event) {
        eventRepository.save(event);
    }
}