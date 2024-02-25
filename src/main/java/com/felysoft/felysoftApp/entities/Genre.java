package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idGenre;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 320, nullable = false)
    private String description;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<Book> books;
}