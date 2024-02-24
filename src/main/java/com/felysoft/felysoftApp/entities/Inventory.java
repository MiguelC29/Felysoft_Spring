package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventory;

    @Column(length = 11, nullable = false)
    private int Stock;

    @Column(length = 30, nullable = false)
    private String type;

    @Column(length = 45, nullable = true)
    private String state;

    @Column(nullable = false)
    private LocalDateTime dateRegistration;

    @Column(nullable = false)
    private LocalDateTime lastModification;

    // FOREIGN KEYS
    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "fkIdProduct", nullable = false)
    //private Product product;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "fkIdBook", nullable = false)
    //private Book book;

    //@ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "fkIdNovelty", nullable = false)
    //private Novelty novelty;

}
