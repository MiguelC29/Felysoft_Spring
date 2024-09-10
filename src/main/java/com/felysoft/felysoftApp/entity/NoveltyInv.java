package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "noveltiesinvetory")
@Entity
public class NoveltyInv implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNovelty;

    @Column(length = 320, nullable = false)
    private String description;

    @Column(length = 11, nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @OneToMany(mappedBy = "noveltyInv", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inventory> inventory;
}
