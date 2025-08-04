package com.vention.examinai.notification_service.listener;

import com.rabbitmq.client.Channel;
import com.vention.examinai.notification_service.dto.email.ConfirmationEmailDto;
import com.vention.examinai.notification_service.dto.email.RegistrationSuccessEmailDto;
import com.vention.examinai.notification_service.enums.EmailExchange;
import com.vention.examinai.notification_service.enums.EmailRoutingKey;
import com.vention.examinai.notification_service.service.RegistrationAndSuccessNotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class EmailNotificationListener {

    private final RegistrationAndSuccessNotificationService emailService;

    public EmailNotificationListener(RegistrationAndSuccessNotificationService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "#{T(com.vention.examinai.notification_service.enums.EmailQueueName).CONFIRMATION_EMAIL.value}")
    public void handleConfirmationEmail(ConfirmationEmailDto confirmationEmailDto) {
        emailService.sendConfirmationEmail(confirmationEmailDto);
    }

    @RabbitListener(queues = "#{T(com.vention.examinai.notification_service.enums.EmailQueueName).REGISTRATION_SUCCESS.value}")
    public void handleRegistrationSuccessEmail(RegistrationSuccessEmailDto registrationSuccessEmailDto) {
        emailService.sendRegistrationSuccessEmail(registrationSuccessEmailDto);
    }

    @RabbitListener(queues = "#{T(com.vention.examinai.notification_service.enums.EmailQueueName).CONFIRMATION_DLQ.value}")
    public void handleDlq(String rawJson, Channel channel, Message message) throws IOException {
        System.out.println("Got from DLQ: " + rawJson);

        channel.basicPublish(
                EmailExchange.DIRECT_EMAIL.getValue(),
                EmailRoutingKey.RETRY.getValue(),
                null,
                rawJson.getBytes(StandardCharsets.UTF_8)
        );

        System.out.println("Sent to retry queue");
    }
}
