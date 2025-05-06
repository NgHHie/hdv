package com.example.service_customer.controllers;

import com.example.service_customer.dto.PurchaseDTO;
import com.example.service_customer.model.Customer;
import com.example.service_customer.service.CustomerService;
import com.example.service_customer.service.PurchaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        log.info("REST request to create Customer : {}", customer);
        Customer createdCustomer = customerService.createCustomer(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        log.info("REST request to get all Customers");
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Integer id,
            @RequestBody Customer customer) {
        log.info("REST request to update Customer : {}", customer);
        Customer updatedCustomer = customerService.updateCustomer(id, customer);
        if (updatedCustomer == null) {
            log.error("Error updating customer with ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        log.info("REST request to delete Customer : {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        log.info("REST request to get Customer by ID : {}", id);
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            log.error("Customer not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/email")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email) {
        log.info("REST request to get Customer by email : {}", email);
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer == null) {
            log.error("Customer not found with email: {}", email);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }




    @GetMapping("/{customerId}/purchase")
    public List<PurchaseDTO> getAllPurchasesWithCustomerId(@PathVariable Integer customerId) {
        log.info("request to get all purchase of customer  : {}", customerId);
        return purchaseService.getPurchasesByCustomerId(customerId);
    }

    @GetMapping("/{customerId}/purchase/{purchaseId}")
    public PurchaseDTO getPurchaseById(@PathVariable Integer purchaseId) {
            return purchaseService.getPurchase(purchaseId);
    }

    @GetMapping("/purchase/{productId}")
    public PurchaseDTO getPurchaseByProductId(@PathVariable Integer productId) {
            return purchaseService.getPurchaseByProductId(productId);
    }

}