package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "noveltiesinvetory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoveltyInv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNovelty;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(length = 11, nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime date;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "noveltyInv", cascade = CascadeType.ALL)
    private List<Inventory> inventory;
}