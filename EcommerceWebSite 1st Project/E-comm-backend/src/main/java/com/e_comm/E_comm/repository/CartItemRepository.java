package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.CartItem;
import com.e_comm.E_comm.model.Cart;
import com.e_comm.E_comm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
