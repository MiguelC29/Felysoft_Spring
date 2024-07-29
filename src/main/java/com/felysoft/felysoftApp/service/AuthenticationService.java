package com.felysoft.felysoftApp.service;

import com.felysoft.felysoftApp.dto.AuthenticationRequest;
import com.felysoft.felysoftApp.dto.RegisterRequest;
import com.felysoft.felysoftApp.dto.ReqRes;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(RegisterRequest authRequest) {
        ReqRes resp = new ReqRes();
        try {
            var user = User.builder()
                    .numIdentification(authRequest.getNumIdentification())
                    .typeDoc(authRequest.getTypeDoc())
                    .names(authRequest.getNames())
                    .lastNames(authRequest.getLastNames())
                    .address(authRequest.getAddress())
                    .phoneNumber(authRequest.getPhoneNumber())
                    .gender(authRequest.getGender())
                    .user_name(authRequest.getUser_name())
                    .email(authRequest.getEmail())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(authRequest.getRole())
                    .build();

            User userResult = userRepository.save(user);
            if (userResult.getIdUser() > 0) {
                resp.setUser((userResult));
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

            /*
            var jwtToken = jwtService.generateToken(user, generateExtraClaims(user));
            return new AuthenticationResponse(jwtToken);
             */
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes login(AuthenticationRequest authRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );

            User user = userRepository.findByEmailAndEliminatedFalse(authRequest.getEmail()).orElseThrow(); // findByUsername

            var jwtToken = jwtService.generateToken(user, generateExtraClaims(user));
            //var refreshToken = jwtService.generateRefreshToken(user, new HashMap<>());
            response.setStatusCode(200);
            response.setToken(jwtToken);
            response.setRole(user.getRole());
            // response.setRefreshToken(refreshToken);
            response.setExpirationTime("3Hrs");
            response.setMessage("Successfully Logged In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRegist) {
        ReqRes response = new ReqRes();
        try {
            String email = jwtService.extractUsername(refreshTokenRegist.getToken());
            User user = userRepository.findByEmailAndEliminatedFalse(email).orElseThrow();
            if (jwtService.isTokenValid(refreshTokenRegist.getToken(), user)) {
                var jwt = jwtService.generateToken(user, generateExtraClaims(user));
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRegist.getToken());
                response.setExpirationTime("3Hrs");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmailAndEliminatedFalse(email);
            if (userOptional.isPresent()) {
                reqRes.setUser(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("User found successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getNames());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("permissions", user.getAuthorities());

        return extraClaims;
    }
}
