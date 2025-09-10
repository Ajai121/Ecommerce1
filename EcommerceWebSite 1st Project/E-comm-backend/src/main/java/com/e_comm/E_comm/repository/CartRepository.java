package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.Cart;
import com.e_comm.E_comm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
