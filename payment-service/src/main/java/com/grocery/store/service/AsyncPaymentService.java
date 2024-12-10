package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.store.dto.Orders;
import com.grocery.store.entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncPaymentService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Listen for Order Events
    @KafkaListener(topics = "order-topic", groupId = "payment-group")
    public void processOrder(String orderMessage) throws JsonProcessingException {
        log.info("Event consumed by Payment Service");
        Orders orderRequest = new ObjectMapper().readValue(orderMessage, Orders.class);

        // Simulate Payment Processing
        boolean paymentSuccess = true; // Mock payment result
        String status = paymentSuccess ? "SUCCESS" : "FAILED";

        // Publish Payment Status
        Payment paymentResponse = new Payment( null, // Auto-generated ID (not needed during processing)
                orderRequest.getId(),
                status,
                100.0, // Mock amount
                "Credit Card");
        kafkaTemplate.send("payment-topic", new ObjectMapper().writeValueAsString(paymentResponse));
        log.info("Event Published by Payment Service {}",paymentResponse.toString());
    }
}
