package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.OrderItem;
import com.e_comm.E_comm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProduct(Product product);
}
