package com.felysoft.felysoftApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
@Entity
public class Service implements Serializable {

    public enum State {ACTIVO, INACTIVO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idService;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

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

    @Column(nullable = false)
    private boolean eliminated;

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
