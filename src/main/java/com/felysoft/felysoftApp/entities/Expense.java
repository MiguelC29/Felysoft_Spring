package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idExpense;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private String description;

    //private long fkIdPurchase;

    //sprivate long fkIdPayment;

    //@OneToOne(targetEntity = Payment.class, cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "fkIdPayment")
    //private Payment payment1;


}
