package com.vention.examinai.notification_service.config;


import com.vention.examinai.notification_service.enums.EmailExchange;
import com.vention.examinai.notification_service.enums.EmailQueueName;
import com.vention.examinai.notification_service.enums.EmailRoutingKey;
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

    public static String NOTIFICATION_EXCHANGE;
    public static String TASK_QUEUE;
    public static String ERROR_QUEUE;
    public static String EXCEPTIONS_TO_ADMINS;
    public static String EXCEPTIONS_TO_ADMINS_CSV;

    private final Environment environment;

    @PostConstruct
    private void postConstruct() {
        NOTIFICATION_EXCHANGE = environment.getProperty("spring.rabbitmq.notification.exchanges.notificationExchange");
        TASK_QUEUE = environment.getProperty("spring.rabbitmq.notification.queues.task");
        ERROR_QUEUE = environment.getProperty("spring.rabbitmq.notification.queues.error");
        EXCEPTIONS_TO_ADMINS = environment.getProperty("spring.rabbitmq.notification.queues.exceptions-text");
        EXCEPTIONS_TO_ADMINS_CSV = environment.getProperty("spring.rabbitmq.notification.queues.exceptions-csv");
    }

    @Bean
    public TopicExchange notificationExchange() {
        return ExchangeBuilder.topicExchange(NOTIFICATION_EXCHANGE)
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

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EmailExchange.DIRECT_EMAIL.getValue());
    }

    @Bean
    public Queue confirmationEmailQueue() {
        return QueueBuilder.durable(EmailQueueName.CONFIRMATION_EMAIL.getValue())
                .withArgument("x-dead-letter-exchange", EmailExchange.DIRECT_EMAIL.getValue())
                .withArgument("x-dead-letter-routing-key", EmailRoutingKey.CONFIRMATION_DLQ.getValue())
                .build();
    }

    @Bean
    public Queue registrationSuccessQueue() {
        return new Queue(EmailQueueName.REGISTRATION_SUCCESS.getValue());
    }

    @Bean
    public Queue confirmationEmailDlqQueue() {
        return new Queue(EmailQueueName.CONFIRMATION_DLQ.getValue());
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(EmailQueueName.RETRY.getValue())
                .withArgument("x-dead-letter-exchange", EmailExchange.DIRECT_EMAIL.getValue())
                .withArgument("x-dead-letter-routing-key", EmailRoutingKey.CONFIRMATION_EMAIL.getValue())
                .withArgument("x-message-ttl", 60000)
                .build();
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
    public Binding confirmationEmailBinding() {
        return BindingBuilder
                .bind(confirmationEmailQueue())
                .to(emailExchange())
                .with(EmailRoutingKey.CONFIRMATION_EMAIL.getValue());
    }

    @Bean
    public Binding registrationSuccessBinding() {
        return BindingBuilder
                .bind(registrationSuccessQueue())
                .to(emailExchange())
                .with(EmailRoutingKey.REGISTRATION_SUCCESS.getValue());
    }

    @Bean
    public Binding confirmationDlqBinding() {
        return BindingBuilder
                .bind(confirmationEmailDlqQueue())
                .to(emailExchange())
                .with(EmailRoutingKey.CONFIRMATION_DLQ.getValue());
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder
                .bind(retryQueue())
                .to(emailExchange())
                .with(EmailRoutingKey.RETRY.getValue());
    }

    @Bean
    public Queue exceptionsTextQueue() {
        return QueueBuilder.durable(EXCEPTIONS_TO_ADMINS).build();
    }

    @Bean
    public Queue exceptionsCsvQueue() {
        return QueueBuilder.durable(EXCEPTIONS_TO_ADMINS_CSV).build();
    }

    @Bean
    public Binding exceptionsTextBinding() {
        return BindingBuilder.bind(exceptionsTextQueue())
                .to(notificationExchange())
                .with(EXCEPTIONS_TO_ADMINS);
    }

    @Bean
    public Binding exceptionsCsvBinding() {
        return BindingBuilder.bind(exceptionsCsvQueue())
                .to(notificationExchange())
                .with(EXCEPTIONS_TO_ADMINS_CSV);
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
