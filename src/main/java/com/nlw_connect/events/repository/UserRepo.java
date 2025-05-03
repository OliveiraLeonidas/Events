package com.nlw_connect.events.repository;

import com.nlw_connect.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {

    public User findByEmail(String email);

    public User findUserById(Integer userId);
}
