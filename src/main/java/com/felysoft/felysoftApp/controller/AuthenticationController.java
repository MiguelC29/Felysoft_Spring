package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.dto.RegisterRequest;
import com.felysoft.felysoftApp.dto.ReqRes;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth/", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.HEAD})
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    @PreAuthorize("permitAll")
    @PostMapping("login")
    public ResponseEntity<ReqRes> login(@RequestBody @Valid AuthenticationRequest authRequest) {
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }

    @PreAuthorize("permitAll")
    @PostMapping("register")
    public ResponseEntity<ReqRes> register(@RequestBody @Valid RegisterRequest authRequest) {
        return ResponseEntity.ok(authenticationService.register(authRequest));
    }

    @PreAuthorize("permitAll")
    @PostMapping("refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody @Valid ReqRes req) {
        return ResponseEntity.ok(authenticationService.refreshToken(req));
    }

    @PreAuthorize("hasAuthority('READ_MY_PROFILE')")
    @GetMapping("adminuser/get-profile")
    public ResponseEntity<ReqRes> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = authenticationService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
