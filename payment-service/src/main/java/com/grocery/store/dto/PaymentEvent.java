package com.grocery.store.dto;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentEvent {


    private Long id;

    private Long orderId; // Associated Order ID
    private String status; // PENDING, SUCCESS, FAILED
    private Double amount; // Payment amount
    private String paymentMethod; // Example: Credit Card, PayPal
}
