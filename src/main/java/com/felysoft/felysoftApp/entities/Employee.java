package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idEmployee;

    @Column(length = 30, nullable = false, unique = true)
    private String specialty;

    @Column(nullable = false)
    private Date dateBirth;

    @Column(nullable = false)
    private BigDecimal salary;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdNumIdentification", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "employees_charges",
            joinColumns = @JoinColumn(name = "fkIdEmployees", referencedColumnName = "idEmployee"),
            inverseJoinColumns = @JoinColumn(name = "fkIdCharges", referencedColumnName = "idCharge")
    )
    private List<Charge> charges;
}