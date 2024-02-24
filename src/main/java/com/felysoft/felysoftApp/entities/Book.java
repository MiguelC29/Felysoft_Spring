package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

    @Column(length = 6,nullable = false)
    private int yearPublication;

    @Column(nullable = false)
    private BigDecimal priceTime;
}