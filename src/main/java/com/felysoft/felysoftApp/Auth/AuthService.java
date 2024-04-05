package com.felysoft.felysoftApp.Auth;

import com.felysoft.felysoftApp.controllers.UserController;
import com.felysoft.felysoftApp.Jwt.JwtService;
import com.felysoft.felysoftApp.entities.User;
import com.felysoft.felysoftApp.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserController userController;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(UserController userController, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.userController = userController;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public AuthResponse login(Map<String, Object> request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userRepository.findByUsername((String) request.get("username")).orElseThrow();
        String token = jwtService.getToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse register(Map<String, Object> request) {
        try {
            // Codificar el password antes de guardarlo
            String password = (String) request.get("password");
            String encodedPassword = passwordEncoder.encode(password);
            request.put("password", encodedPassword);

            ResponseEntity<Map<String, Object>> responseEntity = userController.create(request);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                User user = (User) responseEntity.getBody().get("data");
                String token = jwtService.getToken(user);
                return new AuthResponse(token);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
