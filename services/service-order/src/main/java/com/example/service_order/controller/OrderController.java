package com.example.service_order.controller;

import com.example.service_order.dto.OrderItemDTO;
import com.example.service_order.service.OrderItemService;
import com.example.service_order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/items/with-warranty")
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItemsWithNotNullWarranty() {
        List<OrderItemDTO> items = orderItemService.getAllOrderItemsWithNotNullWarranty();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{orderId}/items/with-warranty")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsWithNotNullWarrantyByOrderId(@PathVariable Long orderId) {
        List<OrderItemDTO> items = orderService.getOrderItemsWithNotNullWarrantyByOrderId(orderId);
        return ResponseEntity.ok(items);
    }
}