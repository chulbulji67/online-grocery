package com.grocery.store.dto;

import lombok.*;


public class PaymentSuccessEvent extends NotificationEvent {
    private String message;
    private String status;

    public PaymentSuccessEvent(String orderId, String recipientEmail, String message, String status) {
        super(orderId, recipientEmail);
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentSuccessEvent{" +
                "orderId='" + getOrderId() + '\'' +
                ", recipientEmail='" + getRecipientEmail() + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

