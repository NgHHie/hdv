package com.example.service_customer.dto.mapper;


import com.example.service_customer.dto.CustomerDTO;
import com.example.service_customer.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {
    
    private final PurchaseMapper purchaseMapper;
    
    @Autowired
    public CustomerMapper(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }
    
    public CustomerDTO toDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        CustomerDTO dto = CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .build();
        
        if (customer.getPurchases() != null) {
            dto.setPurchases(purchaseMapper.toDTOList(customer.getPurchases()));
        }
        
        return dto;
    }
    public List<CustomerDTO> toDTOList(List<Customer> customers) {
        if (customers == null) {
            return null;
        }
        
        return customers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Customer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .build();
    }
}