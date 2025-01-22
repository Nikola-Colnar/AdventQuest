package oop.controllers;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oop.socket.ProposedEvent;

@RestController
public class ChatController {

  private final ChatClient chatClient;

  @Autowired
  public ChatController(ChatClient.Builder builder) {
      this.chatClient = builder.build();
  }

  @GetMapping("/ai/generate")
  public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
     ProposedEvent response = chatClient.prompt().user(msg -> msg.text("Osoba je pri dopisivanju napisala poruku. Pažljivo ju pročitaj:\n\"{poruka}\".\n Je li osoba dala konkretan prijedlog aktivnosti u kojoj bi ona i njeni sugovornici mogli sudjelovati i definirala što će raditi?\n Ako da, odredi kratki naziv toj aktivnosti i odredi kratki opis aktivnosti ako ima dovoljno informacija.\n Odgovori na Hrvatskom jeziku. Ako je osoba navela dan održavanja te aktivnosti, odredi datum tog dana (godinu, redni broj mjeseca i broj dana u mjesecu). Današnji je datum {datum}")
     .params(Map.of("poruka", "idemo peći fritule s marmeladom", "datum", "22.01.2025."))).call().entity(ProposedEvent.class);
     return Map.of("generation", response.toString());
  }
  @GetMapping("/ai/dan")
  public Map getDay(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
     String response = chatClient.prompt().user(msg -> msg.text("Osoba je pri dopisivanju napisala poruku. Pažljivo ju pročitaj:\n\"{poruka}\".\n Je li osoba dala konkretan prijedlog aktivnosti u kojoj bi ona i njeni sugovornici mogli sudjelovati i definirala što će raditi?\n Ako da, odredi kratki naziv toj aktivnosti i odredi kratki opis aktivnosti ako ima dovoljno informacija.\n Odgovori na Hrvatskom jeziku. Ako je osoba navela dan održavanja te aktivnosti, odredi datum tog dana. Današnji je datum {datum}")
     .params(Map.of("poruka", "jucer pečemo fritule s marmeladom", "datum", "22.01.2025."))).call().content();
     return Map.of("generation", response);
  }
}