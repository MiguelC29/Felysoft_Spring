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

    @Column(nullable = false)
    private String nameImg;

    @Column(length = 50, nullable = false)
    private String typeImg;

    @Column(length = 5000000, nullable = false)
    @Lob
    private byte[] image;

    @Column(length = 320, nullable = false, unique = true)
    private String title;

    @Column(length = 320, nullable = false)
    private String editorial;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(length = 6, nullable = false)
    private short yearPublication;

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
