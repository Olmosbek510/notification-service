package com.vention.examinai.notification_service.ipc.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.examinai.notification_service.config.ExceptionReporter;
import com.vention.examinai.notification_service.dto.request.ErrorNotification;
import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.service.NotificationSenderService;
import com.vention.examinai.notification_service.service.PersonalNotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final PersonalNotificationService personalNotificationService;
    private final NotificationSenderService notificationSenderService;
    private final ExceptionReporter exceptionReporter;

    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.task}")
    public void handleTaskNotification(@Valid TaskNotificationRequest taskNotificationRequest, Message rawMessage) throws IOException {
        String traceId = MDC.get("traceId");
        try {
            String typeId = (String) rawMessage.getMessageProperties().getHeaders().get("__TypeId__");

            if (typeId != null && !TaskNotificationRequest.class.getName().equals(typeId)) {
                System.err.println("Skipping message with unexpected type: " + typeId);
                return;
            }

            if (taskNotificationRequest == null) {
                taskNotificationRequest = new ObjectMapper()
                        .readValue(rawMessage.getBody(), TaskNotificationRequest.class);
            }

            System.out.println("Processing task: " + taskNotificationRequest);

            notificationSenderService.sendPersonalNotification(taskNotificationRequest);
            personalNotificationService.save(taskNotificationRequest);

        } catch (Exception e) {
            exceptionReporter.report(
                    ErrorNotification.builder()
                            .traceId(traceId)
                            .occurredAt(LocalDateTime.now())
                            .message(e.getMessage())
                            .stackTrace(Arrays.toString(e.getStackTrace()))
                            .exceptionClass(e.getClass().getName())
                            .build()
            );
            log.error("Message failed and sent to error queue. Skipping retry.", e);
        }
    }

}
