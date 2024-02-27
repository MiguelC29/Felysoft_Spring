package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSale;

    @Column(nullable = false)
    private LocalDateTime dateSale;

    @Column(nullable = false)
    private BigDecimal totalSale;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdPayment", nullable = false)
    private Payment payment;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "sales_detail", joinColumns = @JoinColumn(name = "fkIdSale", referencedColumnName = "idSale"),
            inverseJoinColumns = @JoinColumn(name = "fkIdDetail", referencedColumnName = "idDetail")
    )
    private List<Detail> details;
}