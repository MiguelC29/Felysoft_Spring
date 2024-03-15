package com.felysoft.felysoftApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider implements Serializable {

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

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Purchase> purchases;

    @ManyToMany(mappedBy = "providers")
    @JsonIgnore
    private List<Category> categories;
}