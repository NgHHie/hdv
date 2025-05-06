package com.example.service_product.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.service_product.models.Product;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySerialNumber(String serial);
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCase(String name);
}