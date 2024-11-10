package oop.service;

import oop.model.Event;
import oop.model.EventUsers;
import oop.model.User;
import oop.repository.EventRepository;
import oop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EventUsersServiceTest {
    @Autowired
    private EventUsersService eus;
    @Autowired
    private EventRepository eventRepository;  // Iako je repository, obavezno ga uključite
    @Autowired
    private UserRepository userRepository;  // Ista stvar za UserRepository

    @Test
    void createEventUser() {
        Event event = new Event("skijanje", "1", "22.1.2025.");
        User user = new User("fjd1234ds", "username", "korisnik");
        event = eventRepository.save(event);   // Spremanje Event entiteta u bazu
        user = userRepository.save(user);
        System.out.println("USERUID: " + user.getUid() + "    EventId: " + event.getId());
        EventUsers eu = new EventUsers(event, user);
        System.out.println(eu.getId().toString());
        System.out.println("eventusers: " + eu.toString());

        EventUsers napravljeni = eus.createEventUser(eu);
        System.out.println(eus.getEventUserById(eu.getId()));

    }
}