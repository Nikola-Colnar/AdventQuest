package oop.controllers;

import oop.model.User;
import oop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
//@RequestMapping("/index")
public class IndexController {

    private final UserService userService;    // koristimo userService koji ima sve JPA ugradene metode (extenda i nista vise)

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService; // Injekcija UserService
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String userType) {
        User userMoj = new User(username, password, email, userType);
        System.out.println("Created User: " + userService.createUser(userMoj).toString());
        System.out.println("Found User: " + userService.getUserByEmail("mm@gmail.com"));
        return "redirect:/index.html"; // Vraća index.html sa podacima
    }
}