package com.grocery.store.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {
    private Long orderId;
    private String recipientEmail;
    private String message;
    private String type;

    // Getters and Setters
}

