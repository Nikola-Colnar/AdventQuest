package oop.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import oop.dto.MessageDTO;
import oop.model.Group;
import oop.model.Message;
import oop.model.User;
import oop.service.GroupService;
import oop.service.JWTService;
import oop.service.MessageService;
import oop.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpCookie;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;


@Slf4j
@Component
public class SocketModule implements CommandLineRunner {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;
    
    @Autowired
    private MessageService messageService;

    private final String ADD_EVENT_PROMPT = "Osoba je pri dopisivanju napisala poruku. Pažljivo ju pročitaj:\n\"{poruka}\".\n Je li osoba dala konkretan prijedlog aktivnosti u kojoj bi ona i njeni sugovornici mogli sudjelovati i definirala što će raditi?\n Ako da, odredi kratki naziv toj aktivnosti i odredi kratki opis aktivnosti ako ima dovoljno informacija.\n Odgovori na Hrvatskom jeziku. Ako je osoba navela dan održavanja te aktivnosti, odredi datum tog dana (godinu, redni broj mjeseca i broj dana u mjesecu). Današnji je datum {datum}";
    private User chatBot;
    private ChatClient chatClient;

    
    private final SocketIOServer server;

    @Autowired
    public SocketModule(SocketIOServer server, ChatClient.Builder builder) {
        this.server = server;
        this.chatClient = builder.build();
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
            ProposedEvent evtData = null;
            try {
                evtData = chatbotAnalyzeMessage(data.getPoruka());
                if(evtData.isProposedActivity()) {
                    LocalDate proposedDate = LocalDate.of(evtData.year(), evtData.month(), evtData.day());
                    LocalDate now = LocalDate.now();
                    if(now.isBefore(proposedDate)) {
                        sendChatbotMsgToGroup(evtData.toString(), senderClient);
                    } else {
                        log.info("Proposed date is in the past.");
                    }
                }
            } catch(DateTimeException e) {
                log.info("Given date is invalid.");
            } 
            catch(Exception e) {
                log.info("AI failed to provide answer according to 'ProposedEvent' format for message [{}]", data.getPoruka());
            }
        };
    }

    public void sendToGroup(String uid_sender, String msg, SocketIOClient senderClient) {
        Message novaPoruka = new Message(uid_sender, msg);
        Group grupa = senderClient.get("group");
        User user = (User)senderClient.get("user");
        novaPoruka.setGroup(grupa);
        messageService.createMessage(novaPoruka); // spremi poruku
        int activeUserCount = 0;
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(String.valueOf(grupa.getIdGrupa())).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) { // ne salje natrag posiljatelju
                client.sendEvent("get_message", new MessageDTO(novaPoruka, user.getUsername()));
                System.out.println("sent!\n");
                activeUserCount++;
            }
        }
        log.info("Message sent to {} active users", String.valueOf(activeUserCount));
    }
    public void sendChatbotMsgToGroup(String msg, SocketIOClient invokedByClient) { // invokedByClient je onaj tko je poslao poruku na koju chatbot reagira 
        Message novaAIPoruka = new Message(String.valueOf(chatBot.getId()), msg);
        Group grupa = invokedByClient.get("group");
        novaAIPoruka.setGroup(grupa);
        messageService.createMessage(novaAIPoruka); // spremi poruku
        for (SocketIOClient client : invokedByClient.getNamespace().getRoomOperations(String.valueOf(grupa.getIdGrupa())).getClients()) {
            client.sendEvent("get_message", new MessageDTO(novaAIPoruka, "chatBot"));
        }

    }
    private ProposedEvent chatbotAnalyzeMessage(String message) {
        ProposedEvent response = chatClient.prompt().user(msg -> msg.text(ADD_EVENT_PROMPT)
        .params(Map.of("poruka", message, "datum", LocalDate.now()))).call().entity(ProposedEvent.class);
        return response;
    }

    @Override
    public void run(String... args) {
        User chatBot = new User("chatBot", "chatBot", "chatBot@app");
        try {
            userService.createUser(chatBot);
            log.info("Stvoren korisnik za chatBota");
        } catch(RuntimeException e) {
            log.info("Korisnik za chatBota vec postoji");
        }
        this.chatBot = userService.getUserByUsername("chatBot");
    }
}