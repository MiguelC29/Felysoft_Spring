package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idService;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateModify;

    @Column(nullable = false)
    private BigDecimal priceAdditional;

    @Column(nullable = false)
    private BigDecimal total;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdTypeService", nullable = false)
    private TypeService typeService;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "fkIdReserve")
    //private Reserve reserve;
}
