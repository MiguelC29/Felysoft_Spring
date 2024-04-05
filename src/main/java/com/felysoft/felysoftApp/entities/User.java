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
    public enum Gender {FEMENINO, MASCULINO};
    public enum TypeDoc {CC,TI,CE};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idUser;

    @Column(length = 15, unique = true)
    private Long numIdentification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDoc typeDoc;

    @Column(length = 45, nullable = false)
    private String names;

    @Column(length = 45, nullable = false)
    private String lastNames;

    @Column(length = 45, nullable = false)
    private String address;

    @Column(length = 10, nullable = false)
    private Long phoneNumber;

    @Column(length = 320, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 45, nullable = false)
    private String password;

    private String nameImg;

    @Column(length = 50)
    private String typeImg;

    @Column(length = 5000000)
    @Lob
    private byte[] image;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateRegister;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp lastModification;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdRole", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserve> reserves;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;
}