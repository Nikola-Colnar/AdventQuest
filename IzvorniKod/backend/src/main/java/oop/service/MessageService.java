package oop.service;

import oop.model.Group;
import oop.model.Message;
import oop.model.User;
import oop.repository.MessageRepository;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Metoda za dohvaćanje svih poruka
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Metoda za dohvaćanje korisnika prema ID-u
    public Optional<Message> getMessageById(int id) {
        return messageRepository.findById(id);
    }

    public Message createMessage(Message message) {
        // Jel postoji grupa s tim id
//        if(messageRepository.findById(message.getMessageID()) != null){
//            throw new RuntimeException("Message with this ID already exists");
//        }
        messageRepository.save(message);
        // Ako id nije zauzet, spremamo korisnika
        return messageRepository.save(message);
    }

    public void deleteMessage(int id) {};
}
