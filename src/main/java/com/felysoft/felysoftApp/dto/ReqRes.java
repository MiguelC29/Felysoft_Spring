package com.felysoft.felysoftApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.util.Role;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private Role role;
    private String refreshToken;
    private String expirationTime;
    private User user;
    private List<User> users;
}
