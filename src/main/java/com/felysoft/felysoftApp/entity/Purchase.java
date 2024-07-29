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
@Table(name = "purchases")
@Entity
public class Purchase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPurchase;

    @Column(nullable = false)
    private Timestamp date;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProvider", nullable = false)
    private Provider provider;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expense> expenses;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinTable(
            name = "details_purchases",
            joinColumns = @JoinColumn(name = "fkIdPurchase", referencedColumnName = "idPurchase"),
            inverseJoinColumns = @JoinColumn(name = "fkIdDetail", referencedColumnName = "idDetail")
    )
    private List<Detail> details;
}
