package com.grocery.store.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentSuccessEvent extends NotificationEvent {
    private String message;
    private String status;

    private List<OrderItem> items; // List of items with product codes and quantities

}

