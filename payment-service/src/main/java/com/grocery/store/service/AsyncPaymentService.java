package com.grocery.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grocery.store.dto.Order;

import com.grocery.store.dto.PaymentFailedEvent;
import com.grocery.store.dto.PaymentSuccessEvent;
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
    private KafkaTemplate<String, Object> kafkaTemplate;

    // Listen for Order Events
    @KafkaListener(topics = "order-topic", groupId = "payment-group",  containerFactory = "concurrentKafkaListenerContainerFactory")
    public void processOrder(Order orderRequest) throws JsonProcessingException {
        log.info("Event consumed by Payment Service {}",orderRequest);
//        Orders orderRequest = new ObjectMapper().readValue(orderMessage, Orders.class);

        // Simulate Payment Processing
        boolean paymentSuccess = ThreadLocalRandom.current().nextBoolean(); // Mock payment result

        String status = paymentSuccess ? "SUCCESS" : "FAILED";
        status = "SUCCESS";

        // Publish Payment Status
        if(paymentSuccess){
            log.info("Items that will be published in INVENTORY-SERVICE is {}",orderRequest.getItems());
        PaymentSuccessEvent paymentResponse = new PaymentSuccessEvent( orderRequest.getId()+"", // Auto-generated ID (not needed during processing)

                "chulbulji67@gmail.com","Your Payment is Success" ,"SUCCESS", orderRequest.getItems());
        log.info("Payment Event that will be send is: {}",paymentResponse);

        kafkaTemplate.send("payment-topic", paymentResponse);
        log.info("Payment Success Event send to payment topic is {}", paymentResponse);
        }
        else{
            PaymentFailedEvent paymentFailedEvent = new PaymentFailedEvent( orderRequest.getId()+"", // Auto-generated ID (not needed during processing)
                    "chulbulji67@gmail.com","Your Payment is Failed" ,"FAILED");
            kafkaTemplate.send("payment-topic", paymentFailedEvent);
            log.info("Payment Success Event send to payment topic is {}", paymentFailedEvent);
        }

        log.info("Event Published by Payment Service");
    }
}
