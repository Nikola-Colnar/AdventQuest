package oop.controllers;

import oop.model.Event;
import oop.model.Group;
import oop.model.UserGroup;
import oop.repository.GroupRepository;
import oop.service.GroupService;
import oop.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/user-groups")
@CrossOrigin(origins="*")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private GroupRepository groupRepository;
    //dodavanje korisnika u grupu
    @PostMapping("/{username}/group/{groupId}")
    public ResponseEntity<UserGroup> addUserToGroup(
            @PathVariable String username,
            @PathVariable int groupId) {
        UserGroup userGroup = userGroupService.addUserToGroup(username, groupId);
        return ResponseEntity.ok(userGroup);
    }
    //brisanje korisnika iz grupe
    @DeleteMapping("/user/{username}/group/{groupId}")
    public ResponseEntity<String> deleteUserFromGroup(
            @PathVariable String username,
            @PathVariable int groupId) {
        boolean deleted = userGroupService.deleteUserFromGroup(username, groupId);
        if (deleted) {
            return ResponseEntity.ok("User successfully removed from group.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or group not found.");
        }
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<UserGroup>> getAllUsersByGroupId(@PathVariable int groupId) {
        List<UserGroup> users = userGroupService.getAllUsersByGroupId(groupId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Object>> getAllGroupsByUsername(@PathVariable String username) {
        // Dohvati sve UserGroup entitete
        List<UserGroup> userGroups = userGroupService.getAllGroupsByUsername(username);
        List<Object> responseList = new ArrayList<>();

        // Za svaku UserGroup, dohvatimo naziv grupe prema groupId
        for (UserGroup userGroup : userGroups) {
            Group group = groupRepository.findById(userGroup.getGroupId()).orElse(null);
            if (group != null) {
                // Dodajemo mapu sa groupId i groupName u listu
                responseList.add(new Object() {
                    public final int groupId = userGroup.getGroupId();
                    public final String groupName = group.getNazivGrupa();
                });
            }
        }

        return ResponseEntity.ok(responseList);
    }
}