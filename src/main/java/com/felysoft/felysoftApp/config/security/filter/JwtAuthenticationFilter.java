package com.felysoft.felysoftApp.config.security.filter;

import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.service.JwtService;
import com.felysoft.felysoftApp.service.imp.UserImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserImp userImp;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //1. Obtener el header que contiene el jwt
        final String authHeader = request.getHeader("Authorization"); // Bearer jwt

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { //authHeader.isBlank()
            filterChain.doFilter(request, response);
            return;
        }

        //2. Obtener jwt desde header
        final String jwt = authHeader.substring(7); //authHeader.split(" ")[1];

        //3. Obtener subject/userEmail desde el jwt
        final String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //4. Setear un objeto Authentication dentro del SecurityContext
            User user = (User) userImp.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, user)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //5. Ejecutar el resto de filtros
        filterChain.doFilter(request, response);
    }
}