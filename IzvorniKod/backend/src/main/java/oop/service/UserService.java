package oop.service;

import oop.model.Group;
import oop.model.User;
import oop.repository.GroupRepository;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired JWTService jwtService;
    @Autowired
    private GroupRepository groupRepository;

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
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);  // Pronađi korisnika po password-u

        if (user != null) {
            return user; // Korisnik uspješno prijavljen
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public boolean userExists(User user){
        if(userRepository.findByEmail(user.getEmail()) != null) {
            return true;
        }
        return false;
    }

    //Metoda za prvu registraciju
    // Metoda za registraciju korisnika
    public String createUser(User user) {
        // Provjera da li korisnik već postoji s istim email-om
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("User with this email already exists");
        }

        // Ako username nije zauzet, spremamo korisnika
        user.setPassword(encoder.encode(user.getPassword()));   //enkodiranje passworda
        userRepository.save(user);
        return jwtService.generateToken(user.getUsername());
    }

    public void deleteUser(User user) {
        // Provjera da li korisnik postoji s istim email-om
        if (userRepository.findByEmail(user.getEmail()) == null) {
            throw new RuntimeException("User with this username doesn't exist");
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

    public String verify(User user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());  //vracamo token
        }
        return "Error";
    }

    public List<Object> getGroupsByUserId(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Object> responseList = new ArrayList<>();

            // Prolazimo kroz sve grupe korisnika i dodajemo groupId i groupName u response listu
            user.get().getGroups().forEach(group -> {
                responseList.add(new Object() {
                    public final int groupId = group.getIdGrupa();  // Koristimo groupId
                    public final String groupName = group.getNazivGrupa(); // Koristimo groupName
                });
            });

            return responseList;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<String> getAllUsersByGroupId(int groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        if(group.isPresent()) {
            List<String> listUserNames = new ArrayList<>();
            group.get().getUsers().forEach(user -> listUserNames.add(user.getUsername()));
            return listUserNames;
        }
        throw new RuntimeException("User not found");
    }
}