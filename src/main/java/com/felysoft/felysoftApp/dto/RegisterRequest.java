package com.felysoft.felysoftApp.dto;

import com.felysoft.felysoftApp.entity.User;
import com.felysoft.felysoftApp.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequest {

    private Long numIdentification;
    private User.TypeDoc typeDoc;
    private String names;
    private String lastNames;
    private String address;
    private Long phoneNumber;
    private String email;
    private User.Gender gender;
    private String user_name;
    private String password;
    private Role role;
}
