package oop.service;

import oop.model.Event;
import oop.model.EventUsers;
import oop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class EventUsersServiceTest {
    @Autowired
    private EventUsersService eus;

    @Test
    void createEventUser() {
        Event event = new Event("skijanje", 1, "22.1.2025.");
        User user = new User("username", "password", "nesto@gmail.com", "KORISNIK");
        EventUsers eu = new EventUsers(event, user);
        EventUsers napravljeni = eus.createEventUser(eu);
        System.out.println(eus.getEventUserById(eu.getId()));

    }
}