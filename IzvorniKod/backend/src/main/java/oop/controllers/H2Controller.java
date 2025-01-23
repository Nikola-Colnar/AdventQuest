package oop.controllers;


import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class H2Controller {

    @GetMapping("/h2-console")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        System.out.println("Redirecting to h2-console");
        response.sendRedirect("/h2-console");
    }
}
