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
@Table(name = "books")
@Entity
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idBook;

    @Column(length = 320, nullable = false, unique = true)
    private String title;

    @Column(length = 320, nullable = false)
    private String editorial;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(length = 4, nullable = false)
    private int yearPublication;

    @Column(nullable = false)
    private BigDecimal priceTime;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdAuthor", nullable = false)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdGenre", nullable = false)
    private Genre genre;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserve> reserves;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Detail> details;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inventory> inventories;
}
