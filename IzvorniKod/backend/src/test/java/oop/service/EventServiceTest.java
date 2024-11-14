package oop.service;

import oop.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    public void testCreateEvent() {
         // Stvaramo novi događaj s trenutnim datumom
         Date currentDate = new Date();
         Event event = new Event("Skijanje", currentDate);
         eventService.createEvent(event);

    }
}
