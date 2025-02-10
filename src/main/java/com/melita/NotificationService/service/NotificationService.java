package com.melita.NotificationService.service;

import com.melita.NotificationService.event.NotificationEvent;
import com.melita.NotificationService.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmail(NotificationEvent notificationEvent) {
        log.info("Entering sendEmail method with event: {}", notificationEvent);

        if (notificationEvent == null) {
            log.error("Notification event is null, cannot proceed with email sending.");
            throw new EmailDeliveryException("Notification event cannot be null", null);
        }

        if (!StringUtils.hasText(notificationEvent.getRecipientEmail())) {
            log.error("Recipient email is missing in NotificationEvent: {}", notificationEvent);
            throw new EmailDeliveryException("Recipient email must be provided", null);
        }

        if (!StringUtils.hasText(notificationEvent.getSubject())) {
            log.error("Email subject is missing in NotificationEvent: {}", notificationEvent);
            throw new EmailDeliveryException("Email subject must be provided", null);
        }

        if (!StringUtils.hasText(notificationEvent.getMessage())) {
            log.error("Email message is missing in NotificationEvent: {}", notificationEvent);
            throw new EmailDeliveryException("Email message must be provided", null);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notificationEvent.getRecipientEmail());
            message.setSubject(notificationEvent.getSubject());
            message.setText(notificationEvent.getMessage());

            log.info("Sending email to: {}, Subject: {}", notificationEvent.getRecipientEmail(), notificationEvent.getSubject());
            mailSender.send(message);
            log.info("Email successfully sent to {}", notificationEvent.getRecipientEmail());

        } catch (MailException e) {
            log.error("Failed to send email to {} due to MailException: {}", notificationEvent.getRecipientEmail(), e.getMessage(), e);
            throw new EmailDeliveryException("Failed to send email", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending email to {}: {}", notificationEvent.getRecipientEmail(), e.getMessage(), e);
            throw new EmailDeliveryException("Unexpected error occurred while sending email", e);
        }
    }
}
