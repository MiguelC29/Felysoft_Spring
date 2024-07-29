package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
@Entity
public class Payment implements Serializable {

    //ENUM (ESTADO DEL PAGO)
    public enum State {PENDIENTE, CANCELADO, REEMBOLSADO, VENCIDO}

    //ENUM (METODO DE PAGO)
    public enum MethodPayment {EFECTIVO, NEQUI, TRANSACCION}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodPayment methodPayment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sale> sales;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expense> expenses;
}
