package oop.socket;

import java.net.HttpCookie;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class JWTAuthCheckListener implements AuthorizationListener {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    @Override
    public AuthorizationResult getAuthorizationResult(HandshakeData data) {
        String cookieHeader = data.getHttpHeaders().get("cookie");
            if(cookieHeader != null) {
                List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);
                String token = null;
                String username = null; 
                for(HttpCookie c: cookies) {
                    if("jwtToken".equals(c.getName())) {
                        token = c.getValue();
                        break;
                    }
                }
                if(token != null) {
                    username = jwtService.extractUserName(token);
                    if(username != null) {
                        try {
                            User user = userService.getUserByUsername(username);
                            if(jwtService.validateToken(token, new MyUserDetails(user))) {
                                String grupaIDStr = data.getSingleUrlParam("group");
                                if(grupaIDStr != null) {
                                    int grupaIDInt = Integer.parseInt(grupaIDStr);
                                    Optional<Group> grupa = groupService.findById(grupaIDInt); 
                                    if(grupa.isPresent()) {
                                        if(grupa.get().getUsers().contains(user)) {
                                            log.info("[user {}] was granted access to group chat of [group {}]", user.getUsername(), grupa.get().getNazivGrupa());
                                            return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
                                        } else {
                                            log.info("[user {}] is not member of [group {}] so he may not send messages to group chat", user.getUsername(), grupa.get().getNazivGrupa());
                                        }
                                    } else {
                                        log.info("[user {}] requested to chat in group with non-existing ID={}", user.getUsername(), grupa.get().getNazivGrupa());
                                    }
                                } else {
                                    log.info("[user {}] did not specify the ID of the group to chat in", user.getUsername());
                                }
                            }
                        } catch (Exception e) {
                            log.info("exception with getting user information");
                            return AuthorizationResult.FAILED_AUTHORIZATION;
                        }
                    }
                }
            }
            log.info("Auth request needs to have a JWT cookie to authenticate");
            return AuthorizationResult.FAILED_AUTHORIZATION;
        }
    }
