package com.e_comm.E_comm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;

    private BigDecimal price; // Price at the time of adding to cart

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;


    public CartItem() {}

    public CartItem(Product product, int quantity, BigDecimal price, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.cart = cart;
    }

}
