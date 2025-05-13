package com.nlw_connect.events.repository;

import com.nlw_connect.events.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepo extends JpaRepository<User, String> {

    User findByEmail(String email);
    UserDetails findByUsername(String username);

    User findUserByUsername(String username);

    User findUserById(String userId);
}
