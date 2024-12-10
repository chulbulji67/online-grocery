package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocery.store.dto.PaymentResponse;
import com.grocery.store.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncOrderService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Create Order and Publish Event
    public void createOrder(Orders orderRequest) throws JsonProcessingException {
        // Save order details to the database (optional)
        log.info("Service Method of Create Order");
        kafkaTemplate.send("order-topic", new ObjectMapper().writeValueAsString(orderRequest));
        log.info("event sent to Order topic");
    }

    // Listen for Payment Status Updates
    @KafkaListener(topics = "payment-topic", groupId = "order-group")
    public void handlePaymentResponse(String paymentResponse) throws JsonProcessingException {
        PaymentResponse response = new ObjectMapper().readValue(paymentResponse, PaymentResponse.class);
        log.info("Event Consumed by Order Service {}",response.toString());
        if ("SUCCESS".equals(response.getStatus())) {
            // Update order status to COMPLETED
            log.info("Your Order Placed");
            System.out.println("Order " + response.getOrderId() + " completed successfully!");
        } else {
            // Handle payment failure
            log.warn("Payment Failed");
            System.out.println("Payment failed for order " + response.getOrderId());
        }
    }
}
