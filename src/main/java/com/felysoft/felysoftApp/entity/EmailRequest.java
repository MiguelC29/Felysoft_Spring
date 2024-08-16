package com.felysoft.felysoftApp.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {
    private String emailUser;

    private String subject;

    private String text;

}

