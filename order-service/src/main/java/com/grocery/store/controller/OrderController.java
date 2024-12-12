package com.grocery.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grocery.store.dto.Order;
import com.grocery.store.entity.Orders;
import com.grocery.store.service.AsyncOrderService;
import com.grocery.store.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    AsyncOrderService asyncOrderService;

    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody Orders orders) {
        Orders createdOrders = orderService.placeOrder(orders);
        return ResponseEntity.ok(createdOrders);
    }

    @PostMapping("/async")
    public ResponseEntity<String> asyncPlaceOrder(@RequestBody Order orders) {
        boolean b=false;
        try {
            asyncOrderService.createOrder(orders);
            log.info("Async Place controller called");
            b=true;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

       return ResponseEntity.ok().body(b ?"Order Placed":"Order Failed");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrder(@PathVariable Long id) {
        Optional<Orders> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

