package oop.controllers;

import com.google.api.Http;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SocketHandler;


//PROMIJERNI PORT AKO TREBA

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //ovaj endpoint koristiti za admine!!!
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Spremanje korisnika
        User createdUser = userService.createUser(user);

        // Vraća korisnika u odgovoru sa statusom 201 (Created)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //endpoint za sign up, iz forme za registraciju(React)
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signupUser(
            @RequestBody Map<String, String> userData) {  // Prima cijeli User objekt iz body-a

        System.out.println("IZVRSAVAM SIGNUP");
        try {
            String email = userData.get("email");
            String password = userData.get("password");
            String username = userData.get("username");
            String vrstaUser = userData.get("vrstaUser");

            CreateRequest request = new CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(username);

            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            String uid = userRecord.getUid();

            //Generiraj ID TOKEN na temelju UID-a
            String idToken = FirebaseAuth.getInstance().createCustomToken(uid);

            User user = new User(uid, username, vrstaUser);

            userService.createUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("idToken", idToken);
            response.put("username", username);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (FirebaseAuthException e){
            e.printStackTrace();
            System.out.println("ERROR: " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(
            @RequestHeader("Authorization") String authHeader) {
        System.out.println("IZVRŠAVAM LOGIN");
        String idToken = authHeader.replace("Bearer ", "");
        try {
            // Verificiraj ID token koji je poslan u Authorization headeru
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uidFromToken = decodedToken.getUid();

            // Provjeri postoji li korisnik s tim UID-om u bazi podataka
            User user = userService.loginUser(uidFromToken);
            if (user == null) {
                System.out.println("USERA NEMAAAA ");
                // Ako korisnik nije pronađen, vrati HTTP status UNAUTHORIZED (401)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            // Ako je korisnik pronađen, vrati podatke korisnika s HTTP statusom OK (200)
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            // Ako dođe do greške u verifikaciji tokena, vrati HTTP status UNAUTHORIZED (401)
            System.out.println("LOŠ TI JE TOKEN LUZERU" + e);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
