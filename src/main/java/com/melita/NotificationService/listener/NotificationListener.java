package com.melita.NotificationService.listener;

import com.melita.NotificationService.event.NotificationEvent;
import com.melita.NotificationService.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notification.queue")
    public void receiveNotification(NotificationEvent notificationEvent) {
        log.info("Received notification event for email: {}", notificationEvent.getRecipientEmail());
        notificationService.sendEmail(notificationEvent);
    }
}

