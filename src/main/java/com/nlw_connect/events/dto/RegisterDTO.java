package com.nlw_connect.events.dto;

import com.nlw_connect.events.model.Role;

import java.time.Instant;

public record RegisterDTO(String name, String email, String username, String password, Role role, Instant createdAt) {
}
