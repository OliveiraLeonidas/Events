package com.nlw_connect.events.dto;

import com.nlw_connect.events.model.Role;
import com.nlw_connect.events.model.User;

import java.time.Instant;

public record RegisterResponse(String id, String name, String username, String email) {
    public static RegisterResponse from(User user) {
        return new RegisterResponse(user.getId(), user.getName(), user.getUsername(), user.getEmail());
    }

}
