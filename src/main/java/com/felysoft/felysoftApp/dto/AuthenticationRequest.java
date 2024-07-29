package com.felysoft.felysoftApp.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email; //username
    private String password;
}
