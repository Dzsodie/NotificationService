package com.melita.NotificationService;

import com.melita.NotificationService.event.NotificationEvent;
import com.melita.NotificationService.exception.EmailDeliveryException;
import com.melita.NotificationService.service.NotificationService;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.melita.NotificationService.NotificationServiceTest.TestMailConfig.javaMailSender;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @TestConfiguration
    public static class TestMailConfig {
        @Bean
        public static JavaMailSender javaMailSender() {
            JavaMailSender mockMailSender = Mockito.mock(JavaMailSender.class);
            Mockito.when(mockMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
            return mockMailSender;
        }
    }

    @InjectMocks
    private NotificationService notificationService;

    private NotificationEvent validEvent;

    @BeforeEach
    void setUp() {
        validEvent = new NotificationEvent("test@example.com", "Test Subject", "Test Message");
    }

    @Test
    void sendEmail_Success() {
        doNothing().when(javaMailSender()).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> notificationService.sendEmail(validEvent));

        verify(javaMailSender(), times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_NullEvent_ShouldThrowException() {
        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(null));
        assertEquals("Notification event cannot be null", exception.getMessage());
    }

    @Test
    void sendEmail_EmptyRecipient_ShouldThrowException() {
        validEvent.setRecipientEmail("");

        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(validEvent));
        assertEquals("Recipient email must be provided", exception.getMessage());
    }

    @Test
    void sendEmail_EmptySubject_ShouldThrowException() {
        validEvent.setSubject("");

        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(validEvent));
        assertEquals("Email subject must be provided", exception.getMessage());
    }

    @Test
    void sendEmail_EmptyMessage_ShouldThrowException() {
        validEvent.setMessage("");

        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(validEvent));
        assertEquals("Email message must be provided", exception.getMessage());
    }

    @Test
    void sendEmail_MailException_ShouldThrowException() {
        doThrow(new MailException("SMTP failure") {}).when(javaMailSender()).send(any(SimpleMailMessage.class));

        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(validEvent));
        assertEquals("Failed to send email", exception.getMessage());

        verify(javaMailSender(), times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_GeneralException_ShouldThrowException() {
        doThrow(new RuntimeException("Unexpected error")).when(javaMailSender()).send(any(SimpleMailMessage.class));

        Exception exception = assertThrows(EmailDeliveryException.class, () -> notificationService.sendEmail(validEvent));
        assertEquals("Unexpected error occurred while sending email", exception.getMessage());

        verify(javaMailSender(), times(1)).send(any(SimpleMailMessage.class));
    }
}
