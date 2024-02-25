package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "reserves")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idReserve;

    @Column(nullable = false)
    private LocalDate dateReserve;

    @Column(length = 320)
    private String description;

    @Column(nullable = false)
    private BigDecimal deposit;

    @Column(nullable = false)
    private Time time;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdUser", nullable = false)
    private User user;
}