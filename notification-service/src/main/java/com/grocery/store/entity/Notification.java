package com.grocery.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Associated Order ID
    private String recipientEmail;
    private String message;
    private String type; // CONFIRMATION, DELIVERY_UPDATE, PROMOTION
    private String status; // PENDING, SENT, FAILED

    private LocalDateTime timestamp;

    // Getters and Setters
}

