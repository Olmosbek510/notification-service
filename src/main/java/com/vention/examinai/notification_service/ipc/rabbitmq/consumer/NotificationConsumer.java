package com.vention.examinai.notification_service.ipc.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.examinai.notification_service.config.ExceptionReporter;
import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;
import com.vention.examinai.notification_service.dto.response.PersonalNotificationResponse;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import com.vention.examinai.notification_service.service.PersonalNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final PersonalNotificationService personalNotificationService;
    private final NotificationSenderService notificationSenderService;
    private final ExceptionReporter exceptionReporter;
    private final ObjectMapper objectMapper;
    @RabbitListener(
            id = "${spring.rabbitmq.notification.queues.task-id}",
            queues = "${spring.rabbitmq.notification.queues.task}"
    )
    public void handleTaskNotification(@Valid PersonalNotificationRequest personalNotificationRequest, Message rawMessage) {
        try {
            String typeId = (String) rawMessage.getMessageProperties().getHeaders().get("__TypeId__");

            log.info("Received message from queue: {}", rawMessage.getMessageProperties().getConsumerQueue());

            if (typeId != null && !PersonalNotificationRequest.class.getName().equals(typeId)) {
                log.warn("Skipping message due to unexpected type. Expected: {}, Received: {}",
                        PersonalNotificationRequest.class.getName(), typeId);
                return;
            }

            if (personalNotificationRequest == null) {
                log.debug("Message payload is null, attempting to deserialize raw message body.");
                personalNotificationRequest = objectMapper
                        .readValue(rawMessage.getBody(), PersonalNotificationRequest.class);
            }

            log.info("Processing personal task notification: {}", personalNotificationRequest);

            PersonalNotificationResponse savedNotification = personalNotificationService.save(personalNotificationRequest);
            log.info("Notification saved with ID: {}", savedNotification.id());

            notificationSenderService.sendPersonalNotification(personalNotificationRequest);
            log.info("Notification successfully sent to user with ID: {}", personalNotificationRequest.recipientId());

        } catch (Exception e) {
            exceptionReporter.report(e);
            log.error("Failed to process message. Sending to error queue. Error: {}", e.getMessage(), e);
        }
    }
}
