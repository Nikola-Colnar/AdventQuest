package oop.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import oop.model.Group;
import oop.model.Message;
import oop.model.User;
import oop.service.GroupService;
import oop.service.JWTService;
import oop.service.MessageService;
import oop.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpCookie;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;


@Slf4j
@Component
public class SocketModule {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private MessageService messageService;
    
    private final SocketIOServer server;

    public SocketModule(SocketIOServer server) {
        this.server = server;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onReceived());

    }
    
    private ConnectListener onConnected() {
        return (client) -> {
            // Auth listener je vec provjerio da JWT token postoji, da je korisnik clan navedene grupe i slicno
            HandshakeData data = client.getHandshakeData();
            String cookieHeader = data.getHttpHeaders().get("cookie");
            String[] cookies = cookieHeader.split(";");
            String token = null; 
            for(String c: cookies) {
                String[] nameValue = c.split("=");
                System.out.println( c + "\n");
                if("jwtToken".equals(nameValue[0].trim())) {
                    token = nameValue[1].trim();
                    break;
                }
            }
            String username = jwtService.extractUserName(token);
            String groupID = data.getSingleUrlParam("group");
            User user = userService.getUserByUsername(username);
            Group group = groupService.getGroupById(Integer.parseInt(groupID));
            client.set("user", user);
            client.set("group", group);
            client.joinRoom(groupID);
            log.info("User {} CONNECTED to socket[{}] joined group chat of group {}", username,  client.getSessionId().toString(), groupID);
        };
        
    }
    
    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("\"User DISCONNECTED from socket[{}]\"", client.getSessionId().toString());
        };
    }
    
    private DataListener<Message> onReceived() {
        return (senderClient, data, ackSender) -> {
            User user = (User)senderClient.get("user");
            sendToGroup((String.valueOf(user.getId())), data.getPoruka(), senderClient);
        };
    }

    public void sendToGroup(String uid_sender, String msg, SocketIOClient senderClient) {
        Message novaPoruka = new Message(uid_sender, msg);
        Group grupa = senderClient.get("group");
        novaPoruka.setGroup(grupa);
        messageService.createMessage(novaPoruka); // spremi poruku
        int activeUserCount = 0;
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(String.valueOf(grupa.getIdGrupa())).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) { // ne salje natrag posiljatelju
                client.sendEvent("get_message", novaPoruka);
                System.out.println("sent!\n");
                activeUserCount++;
            }
        }
        log.info("Message sent to {} active users", String.valueOf(activeUserCount));
    }
}