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
@Table(name = "categories")
@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idCategory;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinTable(
            name = "categories_providers",
            joinColumns = @JoinColumn(name = "fkIdCategories", referencedColumnName = "idCategory"),
            inverseJoinColumns = @JoinColumn(name = "fkIdProviders", referencedColumnName = "idProvider"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"fkIdCategories", "fkIdProviders"})
    )
    private List<Provider> providers;
}
