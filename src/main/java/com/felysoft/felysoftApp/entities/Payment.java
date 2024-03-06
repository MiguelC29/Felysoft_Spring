package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    @Column(length = 45, nullable = false)
    private String methodPayment;

    @Column(length = 45, nullable = false)
    private String state;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal total;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Sale> sales;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Expense> expenses;
}