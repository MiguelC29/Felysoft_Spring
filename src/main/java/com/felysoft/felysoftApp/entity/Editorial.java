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
@Table(name = "editorials")
@Entity
public class Editorial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idEditorial;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 320, nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "editorial", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    @OneToMany(mappedBy = "editorial", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Purchase> purchases;

}
