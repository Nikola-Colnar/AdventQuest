package oop.service;

import oop.dto.UserDTO;
import oop.model.Event;
import oop.model.Group;
import oop.model.RatedEvent;
import oop.model.User;
import oop.repository.EventRepository;
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
    private final EventRepository eventRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired JWTService jwtService;
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
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

    public boolean deleteUser(User user) {
        // Treba provjeriti je li user predstavnik u nekoj grupi ili jedini član
        for(Group group : user.getGroups()) {
            if(group.getUsers().size() == 1){ // ako je jedini član brišemo cijelu grupu
                user.removeGroup(group);
                groupRepository.delete(group);
            } else if(group.getidPredstavnika() == user.getId()){      // ako je predstavnik dodjeljujemo to
                User u = getRandUser(group.getIdGrupa());      // nekom drugom useru u toj grupi
                group.setidPredstavnika(u.getId());
                groupRepository.save(group);
            }
        }
        userRepository.delete(user);
        return true;
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

    public List<UserDTO> findAllUsers() {
        List<UserDTO> listUserDTO = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            listUserDTO.add(new UserDTO(user));
        }
        return listUserDTO;
    }

    public UserDTO addAdmin(User user) {
        user.setIsAdmin(1);
        return new UserDTO(userRepository.save(user));
    }

    public User getRandUser(int groupId) {

        List<User> userList = new ArrayList<>(groupRepository.findById(groupId).get().getUsers());

        // Odaberi slučajnog korisnika
        Random random = new Random();
        int randomIndex = random.nextInt(userList.size());

        //user mora biti različit od trenutkog predstavnika kojeg želimo obrisati
        if(userList.get(randomIndex).getId() != groupRepository.findById(groupId).get().getidPredstavnika()){
            return userList.get(randomIndex);
        }
        return getRandUser(groupId); // malo rekurzije
    }
}