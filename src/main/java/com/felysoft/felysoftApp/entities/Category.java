package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idCategory;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "categories_providers",
            joinColumns = @JoinColumn(name = "fkIdCategories", referencedColumnName = "idCategory"),
            inverseJoinColumns = @JoinColumn(name = "fkIdProviders", referencedColumnName = "idProvider")
    )
    private List<Provider> providers;
}