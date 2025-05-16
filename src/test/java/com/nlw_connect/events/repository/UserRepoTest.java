package com.nlw_connect.events.repository;

import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.dto.RegisterDTO;
import com.nlw_connect.events.dto.UserDTO;
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
        UserDTO data = new UserDTO("Leonidas", email, "olivleo", "951357", Role.USER, Instant.now());

        this.createUser(data);
        entityManager.flush();
        Optional<User> foundUser = Optional.ofNullable(this.userRepo.findByEmail(email));

        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Should not get user from database")
    void findByEmailCase2() {
        String wrongEmail = "leo@gmail.com.br";

        Optional<User> result = Optional.ofNullable(this.userRepo.findByEmail(wrongEmail));

        assertThat(result.isEmpty()).isTrue();
    }


    private void createUser(UserDTO data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
//        return newUser;
    }
}