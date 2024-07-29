package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
@Entity
public class Author implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idAuthor;

    @Column(length = 320, nullable = false, unique = true)
    private String name;

    @Column(length = 45, nullable = false)
    private String nationality;

    @Column(nullable = false)
    private Date dateBirth;

    @Column(length = 540, nullable = false)
    private String biography;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Book> books;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private List<Genre> genres;
}
