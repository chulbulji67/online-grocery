package com.grocery.store.dto;

public class PaymentFailedEvent extends NotificationEvent {
    private String message;
    private String status;

    public PaymentFailedEvent(String orderId, String recipientEmail, String message, String status) {
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
        return "PaymentFailedEvent{" +
                "orderId='" + getOrderId() + '\'' +
                ", recipientEmail='" + getRecipientEmail() + '\'' +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
