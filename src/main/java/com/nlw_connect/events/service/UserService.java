package com.nlw_connect.events.service;

import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.dto.*;
import com.nlw_connect.events.infra.security.TokenService;
import com.nlw_connect.events.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepo userRepo;

    public LoginResponseDTO loginAuth(AuthenticationDTO data) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }


    public RegisterResponseDTO registerAuth(UserDTO data) throws Exception {
        User existingUser = this.userRepo.findUserByUsername(data.username());
        if (existingUser != null) throw new Exception("User already exists!");
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data);
        user.setPassword(encryptedPassword);
        user.setCreatedAt(Instant.now());

        this.userRepo.save(user);
        return RegisterResponseDTO.from(user);
    }

}
