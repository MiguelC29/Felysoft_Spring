package com.felysoft.felysoftApp.dto;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Data
public class AuthenticationRequest {

    private String email; //username
    private String password;

    public static Boolean isAdmin() {
        // Obtén la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        // Obtén el rol del usuario desde las autoridades
        String role = authentication.getAuthorities().stream()
                .filter(a -> a.getAuthority().contains("ROLE_"))
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        return role.equals("ROLE_ADMINISTRATOR");
    }
}
