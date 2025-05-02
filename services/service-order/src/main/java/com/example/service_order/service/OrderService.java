package com.example.service_order.service;

import com.example.service_order.dto.OrderItemDTO;
import com.example.service_order.dto.mapper.OrderMapper;
import com.example.service_order.model.Order;
import com.example.service_order.model.OrderItem;
import com.example.service_order.repository.OrderItemRepository;
import com.example.service_order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItemDTO> getOrderItemsWithNotNullWarrantyByOrderId(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();           
            return order.getItems().stream()
                    .filter(item -> item.getWarrantyExpiration() != null)
                    .map(OrderMapper::toOrderItemDTO)
                    .collect(Collectors.toList());
        }
        
        return List.of();
    }
    
    public List<OrderItemDTO> getAllOrderItemsWithNotNullWarranty() {
        List<Order> allOrders = orderRepository.findAll();
        
        return allOrders.stream()
                .flatMap(order -> order.getItems().stream())
                .filter(item -> item.getWarrantyExpiration() != null)
                .map(OrderMapper::toOrderItemDTO)
                .collect(Collectors.toList());
    }
// Trong OrderItemService.java
public List<OrderItemDTO> getOrderItemsWithNotNullWarrantyByUserId(Long userId) {
    
    List<OrderItem> items = orderItemRepository.findByOrderCustomerIdAndWarrantyExpirationIsNotNull(userId);
    return items.stream()
            .map(OrderMapper::toOrderItemDTO)
            .collect(Collectors.toList());         
}
}