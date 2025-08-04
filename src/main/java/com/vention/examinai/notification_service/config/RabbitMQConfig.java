package com.vention.examinai.notification_service.config;


import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.MDC;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

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
        return new Queue(TASK_QUEUE, true);
    }

    @Bean
    public Queue errorQueue() {
        return QueueBuilder.durable(ERROR_QUEUE).build();
    }

    // Only task messages go to taskQueue
    @Bean
    public Binding taskBinding() {
        return BindingBuilder.bind(taskQueue())
                .to(notificationExchange())
                .with("task.#");
    }

    // Only error messages go to errorQueue
    @Bean
    public Binding errorBinding() {
        return BindingBuilder.bind(errorQueue())
                .to(notificationExchange())
                .with("error.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Tracer tracer
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setObservationEnabled(true);
        factory.setMessageConverter(jsonMessageConverter());

        factory.setAdviceChain((MethodInterceptor) invocation -> {
            try {
                if (tracer.currentSpan() != null) {
                    MDC.put("traceId", Objects.requireNonNull(tracer.currentSpan()).context().traceId());
                    MDC.put("spanId", Objects.requireNonNull(tracer.currentSpan()).context().spanId());
                } else {
                    System.err.println("current span is null");
                }
                return invocation.proceed();
            } finally {
                MDC.clear();
            }
        });

        return factory;
    }

}
