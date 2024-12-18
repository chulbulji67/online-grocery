package com.grocery.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Associated Order ID
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount; // Payment amount
    private String paymentMethod; // Example: Credit Card, PayPal
}
