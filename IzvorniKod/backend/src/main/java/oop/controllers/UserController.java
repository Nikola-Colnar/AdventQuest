package oop.controllers;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
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
        public String register(@RequestBody User user, HttpServletResponse response){
        System.out.println("izvrsavam register");
        if (userService.userExists(user)) {
            System.out.println("User already exists.");
            return "User already exists.";
        }

        System.out.println("Stvaram usera");
        String token = userService.createUser(user);
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "Creating user";
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response){
        System.out.println("izvrsavam login");
        try {
            System.out.println("Loginam usera");
            String token = userService.verify(user);
            if(!token.equals("error")){
                Cookie cookie = new Cookie("jwtToken", token);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                response.addCookie(cookie);
                return "Successful login";
            }
        }
        catch (Exception e){
            System.out.println("DOŠLO JE DO GREŠKE " + e.getMessage());
        }
        return "error";
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
