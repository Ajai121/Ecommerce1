package com.e_comm.E_comm.repository;

import com.e_comm.E_comm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByProductNameContainingIgnoreCase(String productName);

    List<Product> findAllByOrderByProductCodeAsc();

    List<Product> findByCategory_NameContainingIgnoreCase(String trimmedName);
}
