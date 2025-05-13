package com.nlw_connect.events.repository;

import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.dto.RegisterDTO;
import com.nlw_connect.events.model.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepoTest {

    // Martin Fowler -> in-memory-database using for tests

    @Autowired
    UserRepo userRepo;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get user successfully from database")
    void findByEmailCase1() {
        String email = "leonidas@gmail.com";
        User data = new User("Leonidas", email, "leooliv", "44444", Role.USER, Instant.now());

        this.createUser(data.getName(), data.getEmail(), data.getUsername(), data.getPassword(), data.getRole(), data.getCreatedAt());
        entityManager.flush();
        Optional<User> foundUser = Optional.ofNullable(this.userRepo.findByEmail(email));

        assertThat(foundUser.isPresent()).isTrue();

    }

    @Test
    void findByUsername() {
    }

    @Test
    void findUserByUsername() {
    }

    @Test
    void findUserById() {
    }


    private User createUser(String name, String email, String username, String encryptedPassword, Role role, Instant createdAt){
        User user = new User(name, email, username, encryptedPassword, role,createdAt);

        this.entityManager.persist(user);

        return user;
    }
}