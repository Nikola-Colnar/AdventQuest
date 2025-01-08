package oop.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
    }

    @GetMapping("/")
    public String greet(HttpServletRequest request){
        return "welkomen" + request.getSession().getId();
    };

    @GetMapping("/students")
        public String studenti(){
        System.out.println(userService.getAllUsers());
        return userService.getAllUsers().toString();
        }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user, HttpServletResponse response) {
        System.out.println("izvrsavam register");

        Map<String, Object> responseBody = new HashMap<>();

        // Provjera postoji li korisnik
        if (userService.userExists(user)) {
            System.out.println("User already exists.");
            responseBody.put("message", "User already exists.");
            responseBody.put("username", user.getUsername()); // Vraća korisničko ime
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody); // HTTP 400 BAD_REQUEST
        }

        // Ako korisnik ne postoji, stvaramo novog korisnika
        System.out.println("Stvaram usera");
        String token = userService.createUser(user);

        // Postavljanje JWT tokena u cookie
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // Vraća samo korisničko ime
        responseBody.put("username", user.getUsername());

        return ResponseEntity.ok(responseBody); // HTTP 200 OK
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user, HttpServletResponse response) {
        System.out.println("izvrsavam login");
        try {
            System.out.println("Loginam usera");
            String token = userService.verify(user);

            if (!token.equals("error")) {
                // Postavljanje JWT tokena u cookie
                Cookie cookie = new Cookie("jwtToken", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);

                // Kreiranje odgovora s korisničkim imenom
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("username", user.getUsername()); // Pretpostavljamo da je korisničko ime dio User objekta
                return ResponseEntity.ok(responseBody);
            }
        } catch (Exception e) {
            System.out.println("DOŠLO JE DO GREŠKE " + e.getMessage());
        }

        // Ako dođe do pogreške, vraćamo JSON s porukom
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Invalid credentials or error occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @GetMapping("/csrf_token")
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");

    }

    @GetMapping("/api/login/google")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

//    @GetMapping("/profile")
//    public String profile(OAuth2AuthenticationToken token, Model model){
//        model.addAttribute()
//    }
    @RequestMapping("/user")
    public Principal user(Principal user){
        return user;
    }
};
