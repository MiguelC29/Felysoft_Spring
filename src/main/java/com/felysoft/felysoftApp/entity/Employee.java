package com.felysoft.felysoftApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
@Entity
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

    @Column(nullable = false)
    private boolean eliminated;

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
