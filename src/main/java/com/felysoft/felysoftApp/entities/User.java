package com.felysoft.felysoftApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "fkIdRole", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserve> reserves;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}