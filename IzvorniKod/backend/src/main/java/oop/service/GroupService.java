package oop.service;

import oop.model.*;
import oop.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepo;
    private final UserService userService;
    public static final int MAX_USERS_IN_GROUP = 21; // Maksimal dopušteni broj korisnika u grupo


    @Autowired
    public GroupService(GroupRepository repo, UserService userService) {
        this.groupRepo = repo;
        this.userService = userService;
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

    public User getRandUser(int groupId) {

        List<User> userList = new ArrayList<>(groupRepo.findById(groupId).get().getUsers());

        // Odaberi slučajnog korisnika
        Random random = new Random();
        int randomIndex = random.nextInt(userList.size());

        //user mora biti različit od trenutkog predstavnika kojeg želimo obrisati
        if(userList.get(randomIndex).getId() != groupRepo.findById(groupId).get().getidPredstavnika()){
            return userList.get(randomIndex);
        }
        return getRandUser(groupId); // malo rekurzije
    }
}
