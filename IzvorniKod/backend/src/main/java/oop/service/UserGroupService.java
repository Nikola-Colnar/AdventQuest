package oop.service;

import oop.model.*;
import oop.repository.UserGroupRepository;
import oop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserGroupService {

    @Autowired
    private UserRepository userRepository; // Provjera korisnika u tablici User

    @Autowired
    private UserGroupRepository userGroupRepository;


    public UserGroup addUserToGroup(String username, int groupId) {
        // Provjeri postoji li korisnik u tablici User
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoSuchElementException("User with username '" + username + "' does not exist.");
        }

        // Provjeri postoji li već zapis s istim username i groupId
        boolean exists = userGroupRepository.existsByUsernameAndGroupId(username, groupId);
        if (exists) {
            throw new IllegalStateException("User '" + username + "' is already in group with ID " + groupId);
        }

        // Dodaj korisnika u grupu
        UserGroup userGroup = new UserGroup();
        userGroup.setUsername(username);
        userGroup.setGroupId(groupId);

        // Spremi u bazu
        return userGroupRepository.save(userGroup);
    }

    public List<UserGroup> getAllUsersByGroupId(int groupId) {
        return userGroupRepository.findByGroupId(groupId);
    }

    public List<UserGroup> getAllGroupsByUsername(String username) {
        return userGroupRepository.findByUsername(username);

    }
}