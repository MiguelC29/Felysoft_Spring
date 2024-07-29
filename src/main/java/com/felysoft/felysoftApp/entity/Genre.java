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
@Table(name = "genres")
@Entity
public class Genre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idGenre;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinTable(
            name = "genres_authors",
            joinColumns = @JoinColumn(name = "fkIdGenres", referencedColumnName = "idGenre"),
            inverseJoinColumns = @JoinColumn(name = "fkIdAuthors", referencedColumnName = "idAuthor")
    )
    private List<Author> authors;
}
