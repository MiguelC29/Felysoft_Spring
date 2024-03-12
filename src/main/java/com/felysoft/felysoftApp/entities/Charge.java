package com.felysoft.felysoftApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "charges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCharge;

    @Column(length = 30, nullable = false)
    private String charge;

    @Column(length = 320, nullable = false)
    private String description;

    // FOREIGN KEYS
    @ManyToMany(mappedBy = "charges")
    @JsonIgnore
    private List<Employee> employees;
}