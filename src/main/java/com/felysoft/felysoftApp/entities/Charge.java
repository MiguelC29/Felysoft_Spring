package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "charges")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCharge;

    @Column(nullable = false)
    private String charge;

    @Column(nullable = false)
    private String description;


}
