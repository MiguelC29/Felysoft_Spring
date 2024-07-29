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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales")
@Entity
public class Sale implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSale;

    @Column(nullable = false)
    public Timestamp dateSale;

    @Column(nullable = false)
    private BigDecimal totalSale;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdPayment", nullable = false)
    private Payment payment;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonIgnore
    @JoinTable(
            name = "details_sales", joinColumns = @JoinColumn(name = "fkIdSale", referencedColumnName = "idSale"),
            inverseJoinColumns = @JoinColumn(name = "fkIdDetail", referencedColumnName = "idDetail")
    )
    private List<Detail> details;
}
