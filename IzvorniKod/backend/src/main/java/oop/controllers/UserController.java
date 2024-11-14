package oop.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

    //ovaj takodjer
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        // Dohvaća sve korisnike
//        List<User> users = userService.getAllUsers();
//
//        // Vraća listu korisnika sa statusom 200 (OK)
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }


    //Endpoint za login, to ce biti na login formi(React)
//    @PostMapping("/login")
//    public ResponseEntity<User> loginUser(@RequestBody User loginDetails) {
//        try {
//            // Prijava na temelju UID-a
//            User user = userService.loginUser(loginDetails.getUid());  // Provjera samo po UID-u
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//    }


    //endpoint za sign up, iz forme za registraciju(React)
    @PostMapping("/signup")
    public ResponseEntity<User> signupUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user) {  // Prima cijeli User objekt iz body-a

        System.out.println("IZVRSAVAM SIGNUP");

        // Provjera je li Authorization header prisutan i ispravan
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("ERRRORRR BROJ 1");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // Loš zahtjev ako header nije ispravan
        }

        // Uklanjanje "Bearer " prefiksa iz Authorization headera
        String idToken = authHeader.replace("Bearer ", "");

        try {
            // Verifikacija ID tokena koristeći Firebase Admin SDK
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uidFromToken = decodedToken.getUid();

            // Postavljanje UID-a iz Firebase tokena
            user.setUid(uidFromToken);  // Postavljamo UID sa Firebase tokena

            // Pozivanje UserService za spremanje novog korisnika u bazu podataka
            User savedUser = userService.createUser(user);

            // Vraćanje korisnika nakon što je uspješno spremljen
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

        } catch (Exception e) {
            // Ako token nije važeći
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("IZVRŠAVAM LOGIN");
        String idToken = authHeader.replace("Bearer ", "");
        try {
            // Verificiraj ID token koji je poslan u Authorization headeru
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uidFromToken = decodedToken.getUid();

            // Provjeri postoji li korisnik s tim UID-om u bazi podataka
            User user = userService.loginUser(uidFromToken);
            if (user == null) {
                System.out.println("User ne postoji");
                // Ako korisnik nije pronađen, vrati HTTP status UNAUTHORIZED (401)
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

            // Ako je korisnik pronađen, vrati podatke korisnika s HTTP statusom OK (200)
            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            // Ako dođe do greške u verifikaciji tokena, vrati HTTP status UNAUTHORIZED (401)
            System.out.println("Nevalja token" + e);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
