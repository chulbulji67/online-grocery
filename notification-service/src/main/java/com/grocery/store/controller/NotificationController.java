package com.grocery.store.controller;

import
        com.grocery.store.dto.NotificationEvent;
import com.grocery.store.dto.NotificationRequest;
import com.grocery.store.dto.PaymentFailedEvent;
import com.grocery.store.dto.PaymentSuccessEvent;
import com.grocery.store.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/test-email")
    public ResponseEntity<String> sendTestEmail(@RequestBody NotificationEvent notificationEvent) {
        if (notificationEvent instanceof PaymentSuccessEvent) {
            notificationService.handleNotification(notificationEvent);
        } else if (notificationEvent instanceof PaymentFailedEvent) {
            notificationService.handleNotification(notificationEvent);
        } else {
            return ResponseEntity.badRequest().body("Invalid notification event type!");
        }
        return ResponseEntity.ok("Test email sent!");
    }

}


