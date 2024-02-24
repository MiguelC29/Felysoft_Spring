package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idEmployee;

    @Column(length = 30, nullable = false,unique = true)
    private String specialty;

    @Column(nullable = false)
    private Date dateBirth;

    @Column(nullable = false)
    private BigDecimal salary;

    //private User fkIdentification;
}