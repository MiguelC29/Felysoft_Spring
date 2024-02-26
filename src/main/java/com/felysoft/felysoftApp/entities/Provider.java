package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idProvider;

    @Column(length = 320, nullable = false, unique = true)
    private String nit;

    @Column(length = 320, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false)
    private int phoneNumber;

    @Column(length = 320, nullable = false)
    private String email;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

}