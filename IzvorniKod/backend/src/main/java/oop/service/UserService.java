package oop.service;

import oop.model.User;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    // Metoda za dohvaćanje svih korisnika
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metoda za dohvaćanje korisnika prema ID-u
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // Metoda za ažuriranje korisnika
    public User updateUser(int id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        user.setPassword(userDetails.getPassword());

        return userRepository.save(user);
    }

    // Metoda za brisanje korisnika
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    ////////////Prijava///////////
    //Metoda za login
    public User loginUser(String usernameOrEmail, String password) {
        User user = userRepository.findByUsername(usernameOrEmail);
        //Ako korisnik nije pronađen po username-u, provjeravamo po mailu
        //User mora imati jedinstven username i mail
        if (user == null) {
            user = userRepository.findByEmail(usernameOrEmail);
        }

        if (user != null && user.getPassword().equals(password)) {
            return user; // Korisnik uspješno prijavljen
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    //Metoda za prvu registraciju
    public User createUser(User user) {
        //Moramo imati jedinstveni username i mail, javlja da je zauzeto vec
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Ako ne postoji, spremamo korisnika
        return userRepository.save(user);
    }
}