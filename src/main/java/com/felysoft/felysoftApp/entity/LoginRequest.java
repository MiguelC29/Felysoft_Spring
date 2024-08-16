package com.felysoft.felysoftApp.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    @NotNull
    private String emailUser;

    @NotNull
    private String body;

    private String code;
}

