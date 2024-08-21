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
@Table(name = "details")
@Entity
public class Detail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetail;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS

    // Relación con Product, Book o Service
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProduct", nullable = true)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook", nullable = true)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdService", nullable = true)
    private Service service;

    // Relación con Purchase
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdPurchase", nullable = false)
    private Purchase purchase;
}
