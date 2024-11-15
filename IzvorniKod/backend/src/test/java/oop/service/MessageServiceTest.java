package oop.service;

import oop.model.Event;
import oop.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    @Test
    void createMessage() {
        Date currentDate = new Date();
        Message message = new Message("uid123457890", currentDate, "MojaPoruka");
        messageService.createMessage(message);
    }
}