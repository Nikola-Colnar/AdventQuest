package oop.service;

import oop.model.*;
import oop.repository.GroupRepository;
import org.hibernate.PersistentObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.*;

@Service
public class GroupService {

    GroupRepository groupRepo;
    public static final int MAX_USERS_IN_GROUP = 21; // Maksimal dopušteni broj korisnika u grupo

    @Autowired
    public GroupService(GroupRepository repo) {
        this.groupRepo = repo;
    }

    public Optional<Group> findById(long id) {
        return groupRepo.findById(id).isEmpty() ? Optional.empty() : Optional.of(groupRepo.findById(id).get());
    }

    public Group changeGroupName(Long id, String newName){
        Optional<Group> group = groupRepo.findById(id);
        if(group.isPresent()){
            Group group1 = group.get();
            group1.setNazivGrupa(newName);
            return groupRepo.save(group1);
        }
        else{
            throw new NoSuchElementException("Group with ID " + id + " not found.");
        }
    }

    public void Put(Long id, User user){ // dodavanj usera u već postojeću grupu
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

    public void Delete(Long id, User user){ // brisanje usera iz grupe
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


}
