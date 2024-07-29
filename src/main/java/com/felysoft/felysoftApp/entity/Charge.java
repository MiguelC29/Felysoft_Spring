package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "charges")
@Entity
public class Charge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCharge;

    @Column(length = 30, nullable = false)
    private String charge;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToMany(mappedBy = "charges")
    @JsonIgnore
    private List<Employee> employees;
}
