package oop.service;

import oop.model.*;
import oop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oop.dto.*;

import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepo;
    private final UserService userService;
    private final RatedEventRepository ratedEventRepo;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventCommentsRepository eventCommentsRepo;

    @Autowired
    public GroupService(GroupRepository repo, UserService userService, RatedEventRepository ratedEventRepo, UserRepository userRepository, EventRepository eventRepository, EventCommentsRepository eventCommentsRepo) {
        this.groupRepo = repo;
        this.userService = userService;
        this.ratedEventRepo = ratedEventRepo;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventCommentsRepo = eventCommentsRepo;
    }

    public Group createGroup(Group group) {
        // Jel postoji grupa s tim id
        if(groupRepo.findById(group.getIdGrupa()).isPresent()){
            throw new RuntimeException("User with this UID already exists");
        }
        // Ako id nije zauzet, spremamo korisnika
        group.addUser(userService.getUserById(group.getidPredstavnika()).get());
        return groupRepo.save(group);
    }

    public Optional<Group> findById(int id) {
        return groupRepo.findById(id).isEmpty() ? Optional.empty() : Optional.of(groupRepo.findById(id).get());
    }

    public void deleteGroupById(int id) {
        groupRepo.deleteById(id);
    }

    //dohvacaevente
    public List<EventDTO> getEventsByGroupId(int groupId) {
        Group group = groupRepo.findById(groupId).get();//Sigurno dobivam postojeću grupu
        List<EventDTO> eventDTOS = new ArrayList<>();
        for (Event event : group.getEvents()) {
            eventDTOS.add(new EventDTO(event));
        }
        return eventDTOS; // Vraća sve događaje povezane s grupom
    }

    public List<User> getUsersByGroupId(int groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group with ID " + groupId + " not found."));
        return new ArrayList<>(group.getUsers()); // Vraća sve korisnike iz grupe
    }

    public Group saveGroup(Group group) {
        return groupRepo.save(group);
    }
    
    public EventComments saveComment(String comment, Event event, User user) {
       return eventCommentsRepo.save(
                new EventComments(user, event, comment));
    }

    public List<GroupDTO> findAllGroups() {
        List<GroupDTO> groupDTOS = new ArrayList<>();
        for(Group group : groupRepo.findAll()){
            groupDTOS.add(new GroupDTO(group.getIdGrupa(), group.getNazivGrupa()));
        }
        return groupDTOS;
    }

    public boolean eventInGroup(int groupId, int eventId) {
        for(Event event : groupRepo.findById(groupId).get().getEvents()) { // vidoviti Lovro zna da grupa s
            if(event.getIdEvent() == eventId) {                             // tim IDjem postoji
                return true;
            }
        }
        return false;
    }

    public Group getGroupById(int grupaIDInt) {
        return groupRepo.findById(grupaIDInt).get();
    }
}
