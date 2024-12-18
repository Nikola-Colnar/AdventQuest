package oop.service;

import oop.model.*;
import oop.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class GroupService {

    private final GroupRepository groupRepo;
    public static final int MAX_USERS_IN_GROUP = 21; // Maksimal dopušteni broj korisnika u grupo

    @Autowired
    public GroupService(GroupRepository repo) {
        this.groupRepo = repo;
    }

    public Group createGroup(Group group) {
        // Jel postoji grupa s tim id
        if(groupRepo.findById(group.getIdGrupa()).isPresent()){
            throw new RuntimeException("User with this UID already exists");
        }
        // Ako id nije zauzet, spremamo korisnika

        return groupRepo.save(group);
    }
    public Optional<Group> findById(int id) {
        return groupRepo.findById(id).isEmpty() ? Optional.empty() : Optional.of(groupRepo.findById(id).get());
    }

    public void deleteGroup(int id) {
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

    public void PutUser(int id, User user){ // dodavanje usera u već postojeću grupu
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            Group group1 = group.get();
            Set<User> trenutniUseri =  group1.getUsers();
            if(trenutniUseri.size() >= MAX_USERS_IN_GROUP){
                throw new RequestDeniedException("Maximum number of users in the group " + group1.getNazivGrupa() + " reached.");
            }
            trenutniUseri.forEach(korisnik -> { // provjera je li user već u toj grupi
                if(korisnik.getId() == user.getId()){
                    throw new RequestDeniedException("User with id " + user.getId() + " already exists.");
                }
            });
            group1.getUsers().add(user); // dodaj ga
            groupRepo.save(group1);
        }
        else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }

    public void DeleteUser(int id, User user){ // brisanje usera iz grupe
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            Group group1 = group.get();
            Set<User> trenutniUseri =  group1.getUsers();
            trenutniUseri.forEach(korisnik -> { // provjera postoji li user u toj grupi
                if(korisnik.getId() == user.getId()){
                    group1.getUsers().remove(user);
                    groupRepo.save(group1);
                }
            });
            throw new NoSuchElementException("User with id " + user.getId() + " not found.");
        }
        else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }

    public void putEvent(int id, Event event){
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            Group group1 = group.get();
            Set<Event> trenutniEvent =  group1.getEvents();
            trenutniEvent.forEach(postojeciEvent -> {
                if(postojeciEvent.getIdEvent() == event.getIdEvent()){
                    throw new RequestDeniedException("Event with id " + event.getIdEvent() + " already exists.");
                }
            });
            event.setGroup(group1);
            group1.getEvents().add(event);
            groupRepo.save(group1);
        }else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }

    public void deleteEvent(int id, Event event){
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            Group group1 = group.get();
            Set<Event> trenutniEvent =  group1.getEvents();
            trenutniEvent.forEach(postojeciEvent -> {
                if(postojeciEvent.getIdEvent() == event.getIdEvent()){
                    group1.getEvents().remove(event);
                    groupRepo.save(group1);
                }
            });
            throw new NoSuchElementException("Event with id " + event.getIdEvent() + " not found.");
        } else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }
}
