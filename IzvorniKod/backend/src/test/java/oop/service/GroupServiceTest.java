package oop.service;

import jakarta.transaction.Transactional;
import oop.model.Event;
import oop.model.Group;
import oop.model.Message;
import oop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
class GroupServiceTest {
    @Autowired
    private GroupService groupService;
    @Test
//    @Transactional // Ova anotacija omogućava da svi entiteti budu sačuvani u okviru jedne transakcije
    void createGroup() {
        Group group = new Group();
        group.setNazivGrupa("Grupa jedan");
        group.setUidPredstavnika("UID_predstavnika");
        User user = new User("uidNoviUserNeki", "UserName");
        Date currentDate = new Date();
        Event event = new Event("Skijanje", currentDate, currentDate);
        Event event2 = new Event("Sanjkanje", currentDate, currentDate);
        Message message = new Message("UID_posiljatelja", currentDate, "MojaPoruka");

        group.addUser(user);
        group.addEvent(event);
        group.addEvent(event2);
        group.addMessage(message);
        System.out.println(group.getEvents());
      //  group.getIdGrupa(); //PRAZNO
        Group createdGroup = groupService.createGroup(group);
        System.out.println(createdGroup.toString());
        System.out.println(group.getIdGrupa()); // 1 ili koji god id
    }
}