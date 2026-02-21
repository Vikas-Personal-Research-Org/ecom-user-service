package com.ecom.user.repository;

import com.ecom.user.model.Role;
import com.ecom.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",
        "eureka.client.enabled=false"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setEmail("repo-test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Repo");
        testUser.setLastName("Test");
        testUser.setRole(Role.BUYER);
        testUser = userRepository.save(testUser);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        Optional<User> found = userRepository.findByEmail("repo-test@example.com");

        assertTrue(found.isPresent());
        assertEquals("repo-test@example.com", found.get().getEmail());
        assertEquals("Repo", found.get().getFirstName());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(found.isPresent());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        boolean exists = userRepository.existsByEmail("repo-test@example.com");

        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void save_ShouldPersistUser() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("password");
        newUser.setFirstName("New");
        newUser.setLastName("User");
        newUser.setRole(Role.SELLER);

        User saved = userRepository.save(newUser);

        assertNotNull(saved.getId());
        assertEquals("new@example.com", saved.getEmail());
        assertNotNull(saved.getCreatedAt());
    }
}
