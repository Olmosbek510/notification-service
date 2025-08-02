package com.vention.examinai.notification_service.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    public static String EXCHANGE_NAME;
    public static String TASK_QUEUE;
    public static String ERROR_QUEUE;
    private final Environment environment;

    @PostConstruct
    private void postConstruct() {
        EXCHANGE_NAME = environment.getProperty("spring.rabbitmq.notification.exchanges.notificationExchange");
        TASK_QUEUE = environment.getProperty("spring.rabbitmq.notification.queues.task");
        ERROR_QUEUE = environment.getProperty("spring.rabbitmq.notification.queues.error");
    }

    @Bean
    public TopicExchange notificationExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue taskQueue() {
        return QueueBuilder.durable(TASK_QUEUE)
                .build();
    }

    @Bean
    public Queue errorQueue() {
        return QueueBuilder.durable(ERROR_QUEUE)
                .build();
    }

    @Bean
    public Binding taskBinding() {
        return BindingBuilder.bind(taskQueue())
                .to(notificationExchange())
                .with("task.*");
    }

    @Bean
    public Binding errorBinding() {
        return BindingBuilder.bind(errorQueue())
                .to(notificationExchange())
                .with("error.*");
    }
}
