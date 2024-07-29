package com.felysoft.felysoftApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expenses")
@Entity
public class Expense implements Serializable {

    //ENUM (TIPO DE GASTO)
    public enum Type {NOMINA, ARRIENDO, SERVICIOS, PROVEEDORES}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExpense;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdPurchase", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdPayment", nullable = false)
    private Payment payment;
}
