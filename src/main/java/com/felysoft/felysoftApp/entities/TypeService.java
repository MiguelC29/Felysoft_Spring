package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "typeServices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idTypeService;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 540, nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "typeService", cascade = CascadeType.ALL)
    private List<Service> services;
}