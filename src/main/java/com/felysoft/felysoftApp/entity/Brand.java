package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBrand;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;
}
