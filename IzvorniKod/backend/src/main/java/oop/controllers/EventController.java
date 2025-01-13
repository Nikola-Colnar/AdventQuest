package oop.controllers;

import oop.model.Event;
import oop.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    private EventService eventService;

    // Dobavljanje svih događaja
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Dobavljanje događaja po ID-u
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Kreiranje novog događaja
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventService.saveEvent(event);
        return ResponseEntity.ok(savedEvent);
    }

    // Ažuriranje postojećeg događaja
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestBody Event eventDetails) {
        Optional<Event> existingEvent = eventService.getEventById(id);

        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setEventName(eventDetails.getEventName());
            event.setStartDate(eventDetails.getStartDate());
            event.setEndDate(eventDetails.getEndDate());
            //event.setGroup(eventDetails.getGroup());

            Event updatedEvent = eventService.saveEvent(event);
            return ResponseEntity.ok(updatedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Brisanje događaja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}