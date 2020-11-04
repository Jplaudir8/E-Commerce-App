package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void verifyFindById() throws Exception {
        // Creating User
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = createUserRequest("Test", "testPassword", "testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Testing user lookup by Id
        final ResponseEntity<User> response2 = userController.findById(u.getId());
        User userFound = response2.getBody();
        assertEquals(200, response2.getStatusCodeValue());
    }

    @Test
    public void verifyFindByUsername() throws Exception {
        // Creating User
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("Test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

        // Looking up User by Username
        final ResponseEntity<User> userFound = userController.findByUserName(u.getUsername());
        assertNotNull(userFound);

        User u2 = userFound.getBody();
        if(u2 != null) {
            assertEquals(0, u.getId());
            assertEquals("Test", u.getUsername());
            assertEquals("thisIsHashed", u.getPassword());
        }
    }

    /**
     * Helper Method to create a UserRequest instance.
     * @param username
     * @param password
     * @param confirmPassword
     * @return
     */
    public CreateUserRequest createUserRequest(String username, String password, String confirmPassword) {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setConfirmPassword(confirmPassword);
        return userRequest;
    }

}
