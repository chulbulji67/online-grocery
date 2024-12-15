package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grocery.store.dto.Order;
import com.grocery.store.dto.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class AsyncPaymentService {
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    // Listen for Order Events
    @KafkaListener(topics = "order-topic", groupId = "payment-group",  containerFactory = "concurrentKafkaListenerContainerFactory")
    public void processOrder(Order orderRequest) throws JsonProcessingException {
        log.info("Event consumed by Payment Service");
//        Orders orderRequest = new ObjectMapper().readValue(orderMessage, Orders.class);

        // Simulate Payment Processing
        boolean paymentSuccess = ThreadLocalRandom.current().nextBoolean(); // Mock payment result

        String status = paymentSuccess ? "SUCCESS" : "FAILED";

        // Publish Payment Status
        if(paymentSuccess){
        PaymentEvent paymentResponse = new PaymentEvent( orderRequest.getId(), // Auto-generated ID (not needed during processing)
                orderRequest.getId(),
                status,
                100.0, // Mock amount
                "Credit Card");
        kafkaTemplate.send("payment-topic", paymentResponse);
        }
        else{
            PaymentEvent paymentResponse = new PaymentEvent( orderRequest.getId(), // Auto-generated ID (not needed during processing)
                    orderRequest.getId(),
                    status,
                    100.0, // Mock amount
                    "Credit Card");
            kafkaTemplate.send("payment-topic", paymentResponse);
        }

        log.info("Event Published by Payment Service");
    }
}
