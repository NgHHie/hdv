package com.example.service_order.dto.mapper;



import com.example.service_order.dto.OrderDTO;
import com.example.service_order.dto.OrderItemDTO;
import com.example.service_order.model.Order;
import com.example.service_order.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    
    public static OrderDTO toOrderDTO(Order order) {
        if (order == null) {
            return null;
        }

        OrderDTO orderDTO = OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .build();

        
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(OrderMapper::toOrderItemDTO)
                    .collect(Collectors.toList());
            orderDTO.setItems(itemDTOs);
        }

        return orderDTO;
    }

  
    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder() != null ? orderItem.getOrder().getId() : null)
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .productCategory(orderItem.getProductCategory())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .warrantyExpiration(orderItem.getWarrantyExpiration())
                .build();
    }

   
    public static Order toOrderEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setCustomerId(orderDTO.getCustomerId());
        order.setOrderNumber(orderDTO.getOrderNumber());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setPaymentMethod(orderDTO.getPaymentMethod());

        return order;
    }

   
    public static OrderItem toOrderItemEntity(OrderItemDTO orderItemDTO, Order order) {
        if (orderItemDTO == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        orderItem.setOrder(order);
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setProductName(orderItemDTO.getProductName());
        orderItem.setProductCategory(orderItemDTO.getProductCategory());
        orderItem.setPrice(orderItemDTO.getPrice());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setWarrantyExpiration(orderItemDTO.getWarrantyExpiration());

        return orderItem;
    }
}