package com.melita.NotificationService.service;

import com.melita.NotificationService.event.NotificationEvent;
import com.melita.NotificationService.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmail(NotificationEvent notificationEvent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notificationEvent.getRecipientEmail());
            message.setSubject(notificationEvent.getSubject());
            message.setText(notificationEvent.getMessage());

            mailSender.send(message);
            log.info("Email successfully sent to {}", notificationEvent.getRecipientEmail());
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", notificationEvent.getRecipientEmail(), e.getMessage());
            throw new EmailDeliveryException("Failed to send email", e);
        }
    }
}

