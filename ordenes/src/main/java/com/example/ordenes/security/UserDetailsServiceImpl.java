package com.example.ordenes.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = getById(username);
        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }
        return User
                .withUsername(username)
                .password(usuario.password())
                .roles(usuario.roles().toArray(new String[0]))
                .build();
    }

    public record Usuario(String username, String password, Set<String> roles) {}

    public static Usuario getById(String username) {
        var password = "$2a$10$TrPxNA15kwy8EZnj3q2C7uFghuMSeUVfjoWDNSn.uKxdATZXtsN7a"; // 'santi' encriptado con BCrypt
        Usuario axel = new Usuario("axel", password, Set.of("USER"));
        Usuario santi = new Usuario("santi", password, Set.of("ADMIN"));
        var usuarios = List.of(axel, santi);
        return usuarios.stream()
                .filter(e -> e.username().equals(username))
                .findFirst()
                .orElse(null);
    }
}
