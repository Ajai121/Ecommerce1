package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.Order;
import com.e_comm.E_comm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
