package com.example.service_customer.controllers;

import com.example.service_customer.dto.CustomerDTO;
import com.example.service_customer.dto.PurchaseItemDTO;
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
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer) {
        log.info("REST request to create Customer : {}", customer);
        CustomerDTO createdCustomer = customerService.createCustomerDTO(customer);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        log.info("REST request to get Customer by ID : {}", id);
        CustomerDTO customer = customerService.getCustomerDTOById(id);
        if (customer == null) {
            log.error("Customer not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        log.info("REST request to get Customer by email : {}", email);
        CustomerDTO customer = customerService.getCustomerDTOByEmail(email);
        if (customer == null) {
            log.error("Customer not found with email: {}", email);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("REST request to get all Customers");
        List<CustomerDTO> customers = customerService.getAllCustomerDTOs();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Integer id,
            @RequestBody Customer customer) {
        log.info("REST request to update Customer : {}", customer);
        CustomerDTO updatedCustomer = customerService.updateCustomerDTO(id, customer);
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

    @GetMapping("/purchases/items/with-warranty")
    public ResponseEntity<List<PurchaseItemDTO>> getAllPurchaseItemsWithWarranty() {
        log.info("REST request to get all purchase items with warranty");
        List<PurchaseItemDTO> items = purchaseService.getAllPurchaseItemsWithWarranty();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/purchases/{purchaseId}/items/with-warranty")
    public ResponseEntity<List<PurchaseItemDTO>> getPurchaseItemsWithWarrantyByPurchaseId(@PathVariable Long purchaseId) {
        log.info("REST request to get purchase items with warranty by purchase ID: {}", purchaseId);
        List<PurchaseItemDTO> items = purchaseService.getPurchaseItemsWithWarrantyByPurchaseId(purchaseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{customerId}/purchases/items/with-warranty")
    public ResponseEntity<List<PurchaseItemDTO>> getPurchaseItemsWithWarrantyByCustomerId(@PathVariable Integer customerId) {
        log.info("REST request to get purchase items with warranty by customer ID: {}", customerId);
        List<PurchaseItemDTO> items = purchaseService.getPurchaseItemsWithWarrantyByCustomerId(customerId);
        return ResponseEntity.ok(items);
    }
}