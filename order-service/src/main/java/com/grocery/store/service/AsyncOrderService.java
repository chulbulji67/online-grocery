package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grocery.store.dto.Payment;
import com.grocery.store.dto.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AsyncOrderService {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    // Create Order and Publish Event
    public void createOrder(Order orderRequest) throws JsonProcessingException {
        // Save order details to the database (optional)
        log.info("Service Method of Create Order");
        kafkaTemplate.send("order-topic", orderRequest);
        log.info("event sent to Order topic");
    }

    // Listen for Payment Status Updates
    @KafkaListener(topics = "payment-topic", groupId = "order-group", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void handlePaymentResponse(Payment paymentResponse) throws JsonProcessingException {
//        PaymentResponse response = new ObjectMapper().readValue(paymentResponse, PaymentResponse.class);
        log.info("Event Consumed by Order Service {}",paymentResponse.toString());
        if ("SUCCESS".equals(paymentResponse.getStatus())) {
            // Update order status to COMPLETED
            log.info("Your Order Placed");
            System.out.println("Order " + paymentResponse.getOrderId() + " completed successfully!");
        } else {
            // Handle payment failure
            log.warn("Payment Failed");
            System.out.println("Payment failed for order " + paymentResponse.getOrderId());
        }
    }
}
