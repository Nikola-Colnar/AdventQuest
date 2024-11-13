package oop.service;

import oop.model.Group;
import oop.model.User;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Metoda za dohvaćanje svih korisnika
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metoda za dohvaćanje korisnika prema ID-u
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }


    ////////////Prijava///////////
    //Metoda za login
    public User loginUser(String uid) {
        User user = userRepository.findByUid(uid);  // Pronađi korisnika po UID-u

        if (user != null) {
            return user; // Korisnik uspješno prijavljen
        } else {
            throw new RuntimeException("User not found");
        }
    }

    //Metoda za prvu registraciju
    // Metoda za registraciju korisnika
    public User createUser(User user) {
        // Provjera da li korisnik već postoji s istim UID-om
        if (userRepository.findByUid(user.getUid()) != null) {
            throw new RuntimeException("User with this UID already exists");
        }

        // Ako UID nije zauzet, spremamo korisnika
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        // Provjera da li korisnik postoji s istim UID-om
        if (userRepository.findByUid(user.getUid()) == null) {
            throw new RuntimeException("User with this UID doesnt exists");
        }
        userRepository.delete(user);
    }

    public User changeUserName(int id, String newName){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User user1 = user.get();
            user1.setUsername(newName);
            return userRepository.save(user1);
        }
        else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }
}