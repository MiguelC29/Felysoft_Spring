package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "charges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCharge;

    @Column(length = 30, nullable = false)
    private String charge;

    @Column(length = 320, nullable = false)
    private String description;

    // FOREIGN KEYS
    @ManyToMany(mappedBy = "charges")
    private List<Employee> employees;
}