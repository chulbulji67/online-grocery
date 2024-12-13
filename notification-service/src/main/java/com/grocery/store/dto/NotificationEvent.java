package com.grocery.store.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class NotificationEvent {
    private Long orderId;
    private String recipientEmail;
    private String message;
    private String type;
}

