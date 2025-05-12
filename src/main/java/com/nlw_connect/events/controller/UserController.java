package com.nlw_connect.events.controller;

import com.nlw_connect.events.dto.*;
import com.nlw_connect.events.infra.security.TokenService;
import com.nlw_connect.events.model.User;
import com.nlw_connect.events.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token  = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterDTO data) {
        if(this.userRepo.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), data.username(), encryptedPassword, data.role(), Instant.now());

        this.userRepo.save(newUser);

        return ResponseEntity.ok(RegisterResponse.from(newUser));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("userId")  String userId) {

        User user = this.userRepo.findUserById(userId);
        if(user == null) return ResponseEntity.notFound().build();

        this.userRepo.delete(user);

        return ResponseEntity.ok(new DeleteUserResponse("user was deleted!").getMessage());
    }
}
