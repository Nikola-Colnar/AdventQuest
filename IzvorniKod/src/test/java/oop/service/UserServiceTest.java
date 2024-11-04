package oop.service;

import oop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User("username", "password", "email@example.com", "Admin");
        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals("username", createdUser.getUsername());
        System.out.println(userService.getUserById(1));
    }


}