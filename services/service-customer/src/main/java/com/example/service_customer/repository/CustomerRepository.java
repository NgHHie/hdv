package com.example.service_customer.repository;



import com.example.service_customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
   
    Optional<Customer> findByEmail(String email);
    
  
    boolean existsByEmail(String email);
    
 
}