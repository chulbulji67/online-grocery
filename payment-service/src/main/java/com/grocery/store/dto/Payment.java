package com.grocery.store.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {


    private Long id;

    private Long orderId; // Associated Order ID
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount; // Payment amount
    private String paymentMethod; // Example: Credit Card, PayPal
}
