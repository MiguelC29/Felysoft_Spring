package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "details")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDetail;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    //private long fkIdProduct;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook")
    private Book book;

    //private long fkIdService;

}
