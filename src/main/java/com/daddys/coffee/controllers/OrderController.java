package com.daddys.coffee.controllers;

import com.daddys.coffee.entities.Order;
import com.daddys.coffee.entities.OrderItem;
import com.daddys.coffee.entities.Product;
import com.daddys.coffee.repositories.OrderItemRepository;
import com.daddys.coffee.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderController(@Autowired OrderRepository orderRepository, @Autowired OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok((List<Order>) orderRepository.findAll());
    }

    @PostMapping(value = "/orders", consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addOrder(@RequestBody Order newOrder) {
        Order savedOrder = orderRepository.save(new Order(0L, LocalDateTime.now(), newOrder.getBuyersEmail(), new HashSet<>()));

        List<OrderItem> newOrderItems = newOrder.getOrderItems().stream().map(orderItem -> new OrderItem(
                orderItem.getProduct(),
                orderItem.getQuantity(), savedOrder)).collect(Collectors.toList());

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(newOrderItems);

        return new ResponseEntity<Order>( new Order(savedOrder.getId(),
                                    savedOrder.getCreationDateTime(),
                                    savedOrder.getBuyersEmail(), new HashSet<>(savedOrderItems)), HttpStatus.CREATED);
    }
}
