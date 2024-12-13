package com.grocery.store.service;

import com.grocery.store.dto.NotificationEvent;
import com.grocery.store.dto.NotificationRequest;
import com.grocery.store.entity.Notification;
import com.grocery.store.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    @Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository ) {
        this.notificationRepository = notificationRepository;

    }

    public Notification createNotification(NotificationEvent request) {
        Notification notification = new Notification();
        notification.setOrderId(request.getOrderId());
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setStatus("PENDING");
        notification.setTimestamp(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public void sendNotification(NotificationRequest request) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .orderId(request.getOrderId())
                .type(request.getType())
                .message(request.getMessage())
                .recipientEmail(request.getRecipientEmail())
                .build();
        // Send notification using Kafka
        kafkaTemplate.send("notification-topic", notificationEvent);
        log.info("Notification Event sent to the notification-topic {}", notificationEvent);
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void processNotification(NotificationEvent request) {
        // Simulate sending email or SMS
        log.info("Notification Event received in notification-topic {}",request.toString());
        System.out.println("Sending notification to: " + request.getRecipientEmail());

        Notification notification = createNotification(request);
        notification.setStatus("SENT");
        notificationRepository.save(notification);
    }
}
