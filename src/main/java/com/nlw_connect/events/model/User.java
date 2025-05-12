package com.nlw_connect.events.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @ColumnDefault("ROLE_USER")
    @Column(name = "role", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public User(String name, String email, String username, String encryptedPassword, Role role, Instant createdAt) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = encryptedPassword;
        this.role = role;
        this.createdAt = createdAt;

    }


    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * @return
     * Quais roles o usuário possui
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.name().equals(Role.ADMIN.getRole())) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * @return
     * Verifica se a conta do usuário está autenticada
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
