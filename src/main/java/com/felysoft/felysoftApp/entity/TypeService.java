package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "typeServices")
@Entity
public class TypeService implements Serializable {

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

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @JsonIgnore
    @OneToMany(mappedBy = "typeService", cascade = CascadeType.ALL)
    private List<Service> services;
}
