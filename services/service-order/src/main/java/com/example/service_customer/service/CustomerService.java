package com.example.service_customer.service;



import java.util.List;

import com.example.service_customer.model.Customer;

public interface CustomerService {
    
   
    Customer createCustomer(Customer customer);
    
   
    Customer getCustomerById(Integer id);
    
   
    Customer getCustomerByEmail(String email);
    
  
    List<Customer> getAllCustomers();
    
    
    Customer updateCustomer(Integer id, Customer Customer);
    
    
    void deleteCustomer(Integer id);
    
    
}