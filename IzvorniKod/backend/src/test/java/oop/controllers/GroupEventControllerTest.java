package oop.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GroupEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRepresentativeIdSuccess() throws Exception {
        mockMvc.perform(get("/api/groups/1/getIdPredstavnik"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    void testGetRepresentativeIdNotFound() throws Exception {
        mockMvc.perform(get("/api/groups/999/getIdPredstavnik"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEventsForGroupSuccess() throws Exception {
        mockMvc.perform(get("/api/groups/1/getEvents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].eventName").value("Gledanje filmova"));
    }

    @Test
    void testGetEventsForGroupNotFound() throws Exception {
        mockMvc.perform(get("/api/groups/999/getEvents"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEventSuccess() throws Exception {
        String eventJson = "{ \"eventName\": \"New Event\", \"description\": \"This is a test event\", \"date\": \"2025-01-01\", \"color\": \"#FF5733\" }";

        mockMvc.perform(post("/api/groups/1/addEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value("New Event"));
    }

    @Test
    void testCreateEventGroupNotFound() throws Exception {
        String eventJson = "{ \"eventName\": \"New Event\", \"description\": \"This is a test event\", \"date\": \"2025-01-01\", \"color\": \"#FF5733\" }";

        mockMvc.perform(post("/api/groups/999/addEvent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEventSuccess() throws Exception {
        mockMvc.perform(delete("/api/groups/1/deleteEvent/6"))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteEventGroupNotFound() throws Exception {
        mockMvc.perform(delete("/api/groups/1232324/deleteEvent/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEventEventNotFound() throws Exception {
        mockMvc.perform(delete("/api/groups/1/deleteEvent/999"))
                .andExpect(status().isNotFound());
    }
}
