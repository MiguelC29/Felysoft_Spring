package com.felysoft.felysoftApp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private LocalDateTime date;

}
