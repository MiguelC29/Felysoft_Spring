package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idProduct;

    private String nameImg;

    @Column(length = 50)
    private String typeImg;

    @Column(length = 5000000)
    @Lob
    private byte[] image;

    @Column(length = 45, nullable = false)
    @NotBlank
    private String name;

    @Column(length = 45, nullable = false)
    private String brand;

    @Column(nullable = false)
    @DecimalMin(value = "0.01")
    private BigDecimal salePrice;

    @Column
    private Date expiryDate;

    @Column(nullable = false)
    private boolean eliminated;

    // FOREIGN KEYS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdCategory", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdProvider", nullable = false)
    private Provider provider;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Inventory> inventories;
}
