package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idBook;

    @Column(length = 320, nullable = false)
    private String title;

    @Column(length = 320, nullable = false, unique = true)
    private String editorial;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(length = 4, nullable = false)
    private int yearPublication;

    @Column(nullable = false)
    private BigDecimal priceTime;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdAuthor", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdGenre", nullable = false)
    private Genre genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Reserve> reserves;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Detail> details;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Inventory> inventories;
}