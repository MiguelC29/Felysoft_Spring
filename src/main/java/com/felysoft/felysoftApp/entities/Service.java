package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idService;

    @Column(length = 320, nullable = false)
    private String state;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateCreation;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp dateModification;

    @Column(nullable = false)
    private BigDecimal priceAdditional;

    @Column(nullable = false)
    private BigDecimal total;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdTypeService", nullable = false)
    private TypeService typeService;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdReserve")
    private Reserve reserve;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Detail> details;
}