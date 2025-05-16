package com.nlw_connect.events.controller;

import com.nlw_connect.events.dto.*;
import com.nlw_connect.events.infra.security.TokenService;
import com.nlw_connect.events.domain.entities.User;
import com.nlw_connect.events.repository.UserRepo;
import com.nlw_connect.events.service.UserService;
import io.swagger.v3.oas.annotations.headers.Header;
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
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
        LoginResponseDTO token = userService.loginAuth(data);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid UserDTO data) {
        try {
            RegisterResponseDTO response = userService.registerAuth(data);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("userId")  String userId) {

        User user = this.userRepo.findUserById(userId);
        if(user == null) return ResponseEntity.notFound().build();

        this.userRepo.delete(user);

        return ResponseEntity.status(200).build();
    }
}
