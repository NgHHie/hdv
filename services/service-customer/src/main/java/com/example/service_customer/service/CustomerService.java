package com.example.service_customer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.service_customer.dto.CustomerDTO;
import com.example.service_customer.dto.mapper.CustomerMapper;
import com.example.service_customer.model.Customer;
import com.example.service_customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
   
    public Customer createCustomer(Customer customer) {
        log.info("Creating new customer with email: {}", customer.getEmail());
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

   
    @Transactional
    public Customer updateCustomer(Integer id, Customer customer) {
        log.info("Updating customer with ID: {}", id);
        
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        
        if (existingCustomer == null) {
            log.error("Customer not found with ID: {}", id);
            return null;
        }
        
        if (!existingCustomer.getEmail().equals(customer.getEmail()) && 
                customerRepository.existsByEmail(customer.getEmail())) {
            log.error("Email already exists: {}", customer.getEmail());
            return null;
        }
        
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setAddress(customer.getAddress());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully");
        
        return updatedCustomer;
    }

  
    @Transactional
    public void deleteCustomer(Integer id) {
        log.info("Deleting customer with ID: {}", id);
        
        if (!customerRepository.existsById(id)) {
            log.error("Customer not found with ID: {}", id); 
            return;
        }
        
        customerRepository.deleteById(id);
        log.info("Customer deleted successfully");
    }

  
    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

  
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    
    public CustomerDTO createCustomerDTO(Customer customer) {
        Customer savedCustomer = createCustomer(customer);
        return customerMapper.toDTO(savedCustomer);
    }

   
    public CustomerDTO getCustomerDTOById(Integer id) {
        Customer customer = getCustomerById(id);
        return customer != null ? customerMapper.toDTO(customer) : null;
    }

   
    public CustomerDTO getCustomerDTOByEmail(String email) {
        Customer customer = getCustomerByEmail(email);
        return customer != null ? customerMapper.toDTO(customer) : null;
    }

  
    public List<CustomerDTO> getAllCustomerDTOs() {
        List<Customer> customers = getAllCustomers();
        return customerMapper.toDTOList(customers);
    }

  
    public CustomerDTO updateCustomerDTO(Integer id, Customer customer) {
        Customer updatedCustomer = updateCustomer(id, customer);
        return updatedCustomer != null ? customerMapper.toDTO(updatedCustomer) : null;
    }
}