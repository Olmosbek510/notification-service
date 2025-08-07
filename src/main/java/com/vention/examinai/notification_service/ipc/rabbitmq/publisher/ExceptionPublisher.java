package com.vention.examinai.notification_service.ipc.rabbitmq.publisher;


import com.vention.examinai.notification_service.config.RabbitMQConfig;
import com.vention.examinai.notification_service.dto.request.ErrorNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExceptionPublisher {
    private final RabbitTemplate rabbitTemplate;


    public void publishException(ErrorNotification notification) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                "error.notification",
                notification
        );
    }
}
