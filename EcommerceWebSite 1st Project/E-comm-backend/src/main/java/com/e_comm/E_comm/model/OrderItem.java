package com.e_comm.E_comm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Keep relation nullable so product deletion won't fail
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Product product;

    // --- Snapshot fields ---
    private String productName;
    private String productCode;
    private String productDetails;
    private BigDecimal productPriceAtPurchase;

    private int quantity;
    private BigDecimal price;  // price * quantity

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    public OrderItem(Product product, int quantity, BigDecimal price, Order order) {
        this.product = product;
        this.productName = product.getProductName();
        this.productCode = product.getProductCode();
        this.productDetails = product.getProductDetails();
        this.productPriceAtPurchase = product.getProductPrice();
        this.quantity = quantity;
        this.price = price;
        this.order = order;
    }
}
