package oop.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // No need for custom MockMvc setup, @AutoConfigureMockMvc handles it
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String randomUsername = generateRandomString(8);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                        .content("{ \"username\": \""  + "\", \"email\": \"" + randomUsername + "@example.com\", \"password\": \"\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(randomUsername));
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                        .content("{ \"username\": \"test1\", \"password\": \"123456\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test1"));
    }




    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                        .content("{ \"username\": \"existingUser\", \"email\": \"existingUser@example.com\", \"password\": \"password123\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User already exists."));
    }

    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Add CSRF token
                        .content("{ \"username\": \"nonExistentUser\", \"password\": \"wrongPassword\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid credentials or error occurred"));
    }

    @Test
    public void testNonExistentFunctionality() throws Exception {
        mockMvc.perform(get("/randomurl"))
                .andExpect(status().isNotFound());
    }

}
