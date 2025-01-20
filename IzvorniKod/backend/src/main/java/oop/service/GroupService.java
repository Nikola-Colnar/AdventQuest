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

    public Group changeGroupName(int id, String newName){
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            group.get().setNazivGrupa(newName);
            return groupRepo.save(group.get());
        }
        else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }

    public Group getGroupById(int id) {
        return groupRepo.findById(id).orElse(null); // Vraća null ako grupa nije pronađena
    }

    //dohvacaevente
    public List<Event> getEventsByGroupId(int groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("Group with ID " + groupId + " not found."));
        return new ArrayList<>(group.getEvents()); // Vraća sve događaje povezane s grupom
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
}
