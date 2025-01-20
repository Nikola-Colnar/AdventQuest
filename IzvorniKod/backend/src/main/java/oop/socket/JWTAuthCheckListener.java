package oop.socket;

import java.net.HttpCookie;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;

import lombok.extern.slf4j.Slf4j;
import oop.model.Group;
import oop.model.MyUserDetails;
import oop.model.User;
import oop.service.GroupService;
import oop.service.JWTService;
import oop.service.UserService;

@Slf4j
@Service
public class JWTAuthCheckListener implements AuthorizationListener {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @Override
    @Transactional
    public AuthorizationResult getAuthorizationResult(HandshakeData data) {
        String cookieHeader = data.getHttpHeaders().get("cookie");
        System.out.println(cookieHeader);
        System.out.println("cookies:\n");
            if(cookieHeader != null) {
                String[] cookies = cookieHeader.split(";");
                String token = null;
                String username = null; 
                for(String c: cookies) {
                    String[] nameValue = c.split("=");
                    System.out.println( c + "\n");
                    if("jwtToken".equals(nameValue[0].trim())) {
                        token = nameValue[1].trim();
                        break;
                    }
                }
                if(token != null) {
                    username = jwtService.extractUserName(token);
                    System.out.println("ime " + username);
                    if(username != null) {
                        try {
                            User user = userService.getUserByUsername(username);
                            if(jwtService.validateToken(token, new MyUserDetails(user))) {
                                String grupaIDStr = data.getSingleUrlParam("group");
                                System.out.println("grupa je " + grupaIDStr);
                                if(grupaIDStr != null) {
                                    int grupaIDInt = Integer.parseInt(grupaIDStr);

                                    // Optional<Group> grupa = groupService.findById(grupaIDInt);
                                    // System.out.println("objekt GRUPA je: " + grupa.get().toString()); 
                                    // if(grupa.isPresent()) {
                                    //     if(grupa.get().getUsers().contains(user)) {
                                    //         log.info("[user {}] was granted access to group chat of [group {}]", user.getUsername(), grupa.get().getNazivGrupa());
                                    //         return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
                                    //     } else {
                                    //         log.info("[user {}] is not member of [group {}] so he may not send messages to group chat", user.getUsername(), grupa.get().getNazivGrupa());
                                    //     }
                                    // } else {
                                    //     log.info("[user {}] requested to chat in group with non-existing ID={}", user.getUsername(), grupa.get().getNazivGrupa());
                                    // }
                                    Group grupa = groupService.getGroupById(grupaIDInt);
                                    List<User> users = groupService.getUsersByGroupId(grupaIDInt);
                                    if(users.contains(user)) {
                                        log.info("[user {}] was granted access to group chat of [group {}]", user.getUsername(), grupa.getNazivGrupa());
                                        return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
                                    } else {
                                        log.info("[user {}] is not member of [group {}] so he may not send messages to group chat", user.getUsername(), grupa.getNazivGrupa());
                                    }
                                } else {
                                    log.info("[user {}] did not specify the ID of the group to chat in", user.getUsername());
                                }
                            }
                        } catch (Exception e) {
                            throw e;
                            // log.info("exception with getting user information");
                            // return AuthorizationResult.FAILED_AUTHORIZATION;
                        }
                    }
                }
            }
            log.info("Auth request needs to have a JWT cookie to authenticate");
            return AuthorizationResult.FAILED_AUTHORIZATION;
        }
    }
