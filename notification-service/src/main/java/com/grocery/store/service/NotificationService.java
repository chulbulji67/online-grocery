package com.grocery.store.service;

import com.grocery.store.dto.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.grocery.store.dto.PaymentSuccessEvent;
import com.grocery.store.dto.PaymentFailedEvent;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Slf4j
@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "notification-topic", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleNotification(NotificationEvent notificationEvent) {
        if (notificationEvent instanceof PaymentSuccessEvent) {
            sendPaymentSuccessEmail((PaymentSuccessEvent) notificationEvent);
        } else if (notificationEvent instanceof PaymentFailedEvent) {
            sendPaymentFailureEmail((PaymentFailedEvent) notificationEvent);
        }
    }

    private void sendPaymentSuccessEmail(PaymentSuccessEvent event) {
        String subject = "Payment Successful for Order " + event.getOrderId();
        String body = "Dear " + event.getRecipientEmail() + ",\n\n" +
                "Your payment for Order ID " + event.getOrderId() +
                " has been successfully processed.\n\nThank you for shopping with us!\n\nBest Regards,\nGrocery Store Team";

        sendEmail(event.getRecipientEmail(), subject, body);
    }

    private void sendPaymentFailureEmail(PaymentFailedEvent event) {
        String subject = "Payment Failed for Order " + event.getOrderId();
        String body = "Dear " + event.getRecipientEmail() + ",\n\n" +
                "Unfortunately, your payment for Order ID " + event.getOrderId() +
                " could not be processed.\n\nPlease try again or contact our support team.\n\nBest Regards,\nGrocery Store Team";

        sendEmail(event.getRecipientEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + to);
            e.printStackTrace();
        }
    }
}



//@Slf4j
//@Service
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    @Autowired
//    private  KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Autowired
//    public NotificationService(NotificationRepository notificationRepository ) {
//        this.notificationRepository = notificationRepository;
//
//    }
//
//    public Notification createNotification(NotificationEvent request) {
//        Notification notification = new Notification();
//        notification.setOrderId(request.getOrderId());
//        notification.setRecipientEmail(request.getRecipientEmail());
//        notification.setMessage(request.getMessage());
//        notification.setType(request.getType());
//        notification.setStatus("PENDING");
//        notification.setTimestamp(LocalDateTime.now());
//        return notificationRepository.save(notification);
//    }
//
//    public void sendNotification(NotificationRequest request) {
//        NotificationEvent notificationEvent = NotificationEvent.builder()
//                .orderId(request.getOrderId())
//                .type(request.getType())
//                .message(request.getMessage())
//                .recipientEmail(request.getRecipientEmail())
//                .build();
//        // Send notification using Kafka
//        kafkaTemplate.send("notification-topic", notificationEvent);
//        log.info("Notification Event sent to the notification-topic {}", notificationEvent);
//    }
//
//    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
//    public void processNotification(NotificationEvent request) {
//        // Simulate sending email or SMS
//        log.info("Notification Event received in notification-topic {}",request.toString());
//        System.out.println("Sending notification to: " + request.getRecipientEmail());
//
//        Notification notification = createNotification(request);
//        notification.setStatus("SENT");
//        notificationRepository.save(notification);
//    }
//}
