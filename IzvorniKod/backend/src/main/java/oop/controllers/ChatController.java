package oop.controllers;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  @Autowired
  public ChatController(ChatClient.Builder builder) {
      this.chatClient = builder.build();
  }

  @GetMapping("/ai/generate")
  public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
     String response = chatClient.prompt().user(message).call().content();
     return Map.of("generation", response);
  }
}