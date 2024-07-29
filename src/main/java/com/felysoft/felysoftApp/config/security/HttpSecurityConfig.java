package com.felysoft.felysoftApp.config.security;

import com.felysoft.felysoftApp.config.security.filter.JwtAuthenticationFilter;
import com.felysoft.felysoftApp.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                //.authorizeHttpRequests(builderRequestMatchers()
                .sessionManagement(sessionManConfig -> sessionManConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> builderRequestMatchers() {
        return authConfig -> authConfig
                .requestMatchers(HttpMethod.POST, "/api/auth/authenticate").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/public-access").permitAll()
                .requestMatchers("/error").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name())
                .requestMatchers(HttpMethod.POST, "/api/products").hasAuthority(Permission.CREATE_ONE_PRODUCT.name())

                .anyRequest().denyAll();
    }
}
