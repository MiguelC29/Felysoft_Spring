package com.felysoft.felysoftApp.controller;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.dto.RegisterRequest;
import com.felysoft.felysoftApp.dto.ReqRes;
import com.felysoft.felysoftApp.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
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
    @GetMapping("get-profile")
    public ResponseEntity<ReqRes> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = authenticationService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD_USER')")
    @PutMapping("changePassword")
    public ResponseEntity<ReqRes> changePassword(@RequestBody Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String oldPassword = request.get("oldPassword").toString();
        String newPassword = request.get("newPassword").toString();
        return ResponseEntity.ok(this.authenticationService.changePassword(email, oldPassword, newPassword));
    }

    @PreAuthorize("permitAll")
    @GetMapping("verify-account/{token}")
    public ResponseEntity<ReqRes> verifyAccount(@PathVariable("token") String token) {
        return ResponseEntity.ok(authenticationService.verifyAccount(token));
    }

    @PreAuthorize("permitAll")
    @GetMapping("verify-user")
    public ResponseEntity<ReqRes> verifyUser(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(authenticationService.verifyUser(request.get("email").toString()));
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD_USER')")
    @PutMapping("resetPassword/{email}")
    public ResponseEntity<ReqRes> resetPassword(@PathVariable("token") String token, @RequestBody Map<String, Object> request) {
        String newPassword = request.get("newPassword").toString();
        return ResponseEntity.ok(this.authenticationService.resetPassword(token, newPassword));
    }
}
