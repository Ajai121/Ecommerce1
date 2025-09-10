package com.e_comm.E_comm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;

    private String productDetails;

    private String productCode;

    @Column(nullable = false)
    private BigDecimal productPrice;

    private String imageUrl;   // ✅ URL (can be external or internal)

    @Column(name = "is_active")
    private Boolean isActive = true;   // ✅ default true

    private int productQuantity;

    // Many products → one category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
