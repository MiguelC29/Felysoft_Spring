package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventory;

    @Column(length = 11, nullable = false)
    private int stock;

    @Column(length = 30, nullable = false)
    private String typeInv;

    @Column(length = 45)
    private String state;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateRegister;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp lastModification;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProduct", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdNovelty", nullable = false)
    private NoveltyInv noveltyInv;
}