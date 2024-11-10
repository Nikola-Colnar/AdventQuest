package oop.service;

import oop.model.EventUsers;
import oop.model.EventUserId;  // Ne zaboravi importirati ovu klasu
import oop.repository.EventUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventUsersService {
    private final EventUsersRepository eventUsersRepository;

    @Autowired
    public EventUsersService(EventUsersRepository eventUsersRepository) {
        this.eventUsersRepository = eventUsersRepository;
    }

    // Metoda za dodavanje novog EventUsers entiteta
    public EventUsers createEventUser(EventUsers eventUsers) {
        return eventUsersRepository.save(eventUsers);
    }

    // Metoda za dohvaćanje svih EventUsers
    public List<EventUsers> getAllEventUsers() {
        return eventUsersRepository.findAll();
    }

    // Metoda za dohvaćanje EventUsers prema ID-u
    public Optional<EventUsers> getEventUserById(EventUserId id) {
        return eventUsersRepository.findById(id);
    }

    // Metoda za ažuriranje EventUsers entiteta
//    public EventUsers updateEventUser(EventUserId id, EventUsers eventUsersDetails) {
//        EventUsers eventUsers = eventUsersRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("EventUser not found with id " + id));
//
//        // Ažuriraj potrebne detalje
//        eventUsers.setEvent(eventUsersDetails.getEvent());
//        eventUsers.setUser(eventUsersDetails.getUser());
//
//        return eventUsersRepository.save(eventUsers);
//    }

    // Metoda za brisanje EventUsers
    public void deleteEventUser(EventUserId id) {
        eventUsersRepository.deleteById(id);
    }
}