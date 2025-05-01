package com.example.service_order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.service_order.dto.OrderItemDTO;
import com.example.service_order.dto.mapper.OrderMapper;
import com.example.service_order.model.OrderItem;
import com.example.service_order.repository.OrderItemRepository;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderItemDTO> getAllOrderItemsWithNotNullWarranty() {
        List<OrderItem> items = orderItemRepository.findAllWithNotNullWarrantyExpiration();
        return items.stream()
                .map(OrderMapper::toOrderItemDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderItemDTO> getOrderItemsByOrderIdWithNotNullWarranty(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderIdAndNotNullWarrantyExpiration(orderId);
        return items.stream()
                .map(OrderMapper::toOrderItemDTO)
                .collect(Collectors.toList());
    }
    
    public List<OrderItemDTO> filterOrderItemsWithNotNullWarranty(List<OrderItem> orderItems) {
        return orderItems.stream()
                .filter(item -> item.getWarrantyExpiration() != null)
                .map(OrderMapper::toOrderItemDTO)
                .collect(Collectors.toList());
    }
}