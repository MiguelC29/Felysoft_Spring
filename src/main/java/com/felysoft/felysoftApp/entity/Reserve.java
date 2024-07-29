package com.felysoft.felysoftApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reserves")
@Entity
public class Reserve implements Serializable {

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

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdUser", nullable = false)
    private User user;
}
