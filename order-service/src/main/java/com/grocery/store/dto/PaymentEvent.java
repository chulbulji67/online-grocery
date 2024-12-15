package com.grocery.store.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentEvent {
    private Long id;

    private Long orderId; // Associated Order ID
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount; // Payment amount
    private String paymentMethod; // Example: Credit Card, PayPal
}
