package com.grocery.store.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PaymentSuccessEvent.class, name = "PaymentSuccessEvent"),
        @JsonSubTypes.Type(value = PaymentFailedEvent.class, name = "PaymentFailedEvent")
})
public abstract class NotificationEvent {
    private String orderId;
    private String recipientEmail;

//    public NotificationEvent(){
//
//    }

    public NotificationEvent(String orderId, String recipientEmail) {
        this.orderId = orderId;
        this.recipientEmail = recipientEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}


