package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {
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
}