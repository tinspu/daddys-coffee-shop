package com.daddys.coffee.controllers;

import com.daddys.coffee.entities.Order;
import com.daddys.coffee.entities.OrderItem;
import com.daddys.coffee.entities.Product;
import com.daddys.coffee.repositories.OrderItemRepository;
import com.daddys.coffee.repositories.OrderRepository;
import com.daddys.coffee.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final UserInfoRepository userInfoRepository;
    public OrderController(@Autowired OrderRepository orderRepository,
                           @Autowired OrderItemRepository orderItemRepository,
                           @Autowired UserInfoRepository userInfoRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getOrders(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok((Page<Order>) orderRepository.findAllByBuyersEmail(
                userInfoRepository.findByName(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get().getEmail(),
                PageRequest.of(page, size)
        ));
    }

    @PostMapping(value = "/orders", consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addOrder(@RequestBody Order newOrder) {
        Order savedOrder = orderRepository.save(
                new Order(0L, LocalDateTime.now(),
                        userInfoRepository.findByName(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()).get().getEmail(),
                        new HashSet<>()));

        List<OrderItem> newOrderItems = newOrder.getOrderItems().stream().map(orderItem -> new OrderItem(
                orderItem.getProduct(),
                orderItem.getQuantity(), savedOrder)).collect(Collectors.toList());

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(newOrderItems);

        return new ResponseEntity<Order>( new Order(savedOrder.getId(),
                                    savedOrder.getCreationDateTime(),
                                    savedOrder.getBuyersEmail(), new HashSet<>(savedOrderItems)), HttpStatus.CREATED);
    }
}
