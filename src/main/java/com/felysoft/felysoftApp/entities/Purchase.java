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
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPurchase;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal total;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProvider", nullable = false)
    private Provider provider;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "details_purchases",
            joinColumns = @JoinColumn(name = "fkIdPurchase", referencedColumnName = "idPurchase"),
            inverseJoinColumns = @JoinColumn(name = "fkIdDetail", referencedColumnName = "idDetail")
    )
    private List<Detail> details;
}