package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

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
    //@OneToOne(targetEntity = Expense.class, cascade = CascadeType.ALL)
    //@JoinColumn(name = "fkIdExpense")
    //private Expense expense;

    //@OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    //private List<Expense> expenses = new ArrayList<>();
}