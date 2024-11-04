package oop.service;

import oop.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        Event event = new Event("naziv", 2 , "datum");
        Event createdEvent = eventService.createEvent(event);
        assertNotNull(createdEvent);
        System.out.println(eventService.getEventById(1));
    }

}