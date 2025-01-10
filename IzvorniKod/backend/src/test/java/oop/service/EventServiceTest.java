package oop.service;

import oop.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Calendar;
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

         Calendar calendar = Calendar.getInstance();
         calendar.setTime(currentDate);
         calendar.add(Calendar.DAY_OF_MONTH, 1);
         Date tomorrow = calendar.getTime();





    }
}
