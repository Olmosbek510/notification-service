package com.vention.examinai.notification_service.config;

import com.vention.examinai.notification_service.dto.request.ErrorNotification;
import com.vention.examinai.notification_service.ipc.rabbitmq.publisher.ExceptionPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionReporter {
    private final ExceptionPublisher exceptionPublisher;
    private final Environment environment;
    public void report(ErrorNotification notification){
        notification.setServiceName(environment.getProperty("spring.application.name"));
        exceptionPublisher.publishException(notification);
    }
}
