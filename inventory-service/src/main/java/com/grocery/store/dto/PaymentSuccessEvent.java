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

    // Constructor
//    public PaymentSuccessEvent(String orderId, String recipientEmail, String message, String status, List<OrderItem> items) {
//        super(orderId, recipientEmail);
//        this.items = items;
//        this.message = message;
//        this.status = status;
//    }
//
//    public PaymentSuccessEvent(String orderId, String recipientEmail, String message, String status) {
//        super(orderId, recipientEmail);
//        this.message = message;
//        this.status = status;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

//    @Override
//    public String toString() {
//        return "PaymentSuccessEvent{" +
//                "orderId='" + getOrderId() + '\'' +
//                ", recipientEmail='" + getRecipientEmail() + '\'' +
//                ", message='" + message + '\'' +
//                ", status='" + status + '\'' +
//                '}';
//    }
}

