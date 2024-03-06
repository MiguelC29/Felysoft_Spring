package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idRole;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<User> user;
}