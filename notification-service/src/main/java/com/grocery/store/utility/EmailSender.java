package com.grocery.store.utility;

import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    public void sendEmail(String recipient, String subject, String body) {
        // Use a library like JavaMailSender or any email service API
        System.out.println("Sending email to: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
}

