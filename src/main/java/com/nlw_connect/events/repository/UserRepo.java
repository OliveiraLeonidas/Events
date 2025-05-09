package com.nlw_connect.events.repository;

import com.nlw_connect.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {

    User findByEmail(String email);

    User findUserById(String userId);
}
