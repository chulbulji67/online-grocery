package com.grocery.store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Associated Order ID
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount; // Payment amount
    private String paymentMethod; // Example: Credit Card, PayPal
}
