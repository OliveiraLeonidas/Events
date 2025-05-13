package com.nlw_connect.events.dto;

import com.nlw_connect.events.domain.entities.User;

public record RegisterResponseDTO(String id, String name, String username, String email) {
    public static RegisterResponseDTO from(User user) {
        return new RegisterResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail());
    }

}
