package com.vention.examinai.notification_service.ipc.rabbitmq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import com.vention.examinai.notification_service.service.PersonalNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final PersonalNotificationService personalNotificationService;
    private final ObjectMapper objectMapper;
    private final NotificationSenderService notificationSenderService;

    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.task}")
    public void handleTaskNotification(String message) {
        try {
            TaskNotificationRequest taskNotificationRequest =
                    objectMapper.readValue(message, TaskNotificationRequest.class);
            System.out.println(taskNotificationRequest);
            notificationSenderService.sendPersonalNotification(taskNotificationRequest);

            personalNotificationService.save(taskNotificationRequest);


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
