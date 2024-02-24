package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(unique = true)
    private Long numIdentification;

    @Column(length = 20, nullable = false)
    private String typeDoc;

    @Column(length = 45, nullable = false)
    private String names;

    @Column(length = 45, nullable = false)
    private String lastNames;

    @Column(length = 45, nullable = false)
    private String address;

    @Column(length = 20, nullable = false)
    private int phoneNumber;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String gender;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 45, nullable = false)
    private String password;

    @Column
    private byte[] image;

    @Column(length = 50)
    private String typeImg;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateRegister;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp lastModification;

    // FOREIGN KEYS
    /*
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdRole")
    private Role role;*/
}