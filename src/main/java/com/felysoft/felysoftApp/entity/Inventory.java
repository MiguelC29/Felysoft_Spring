package com.felysoft.felysoftApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventories")
@Entity
public class Inventory implements Serializable {
    public enum TypeInv {PRODUCTOS, LIBROS}
    public enum State {DISPONIBLE, BAJO, AGOTADO, RESERVADO}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventory;

    @Column(length = 11, nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeInv typeInv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateRegister;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp lastModification;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProduct", unique = true)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdBook", unique = true)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdNovelty", unique = true)
    private NoveltyInv noveltyInv;
}
