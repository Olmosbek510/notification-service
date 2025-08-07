package com.vention.examinai.notification_service.config;

import com.vention.examinai.notification_service.dto.request.ErrorNotification;
import com.vention.examinai.notification_service.ipc.rabbitmq.publisher.ExceptionPublisher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ExceptionReporter {
    private final ExceptionPublisher exceptionPublisher;
    private final Environment environment;

    public void report(Exception ex) {
        String traceId = MDC.get("traceId");
        var errorNotification = ErrorNotification.builder()
                .traceId(traceId)
                .exceptionClass(ex.getClass().getName())
                .message(ex.getMessage())
                .stackTrace(Arrays.toString(ex.getStackTrace()))
                .occurredAt(LocalDateTime.now())
                .serviceName(environment.getProperty("spring.application.name"))
                .build();
        exceptionPublisher.publishException(errorNotification);
    }

    public void report(Exception ex, HttpServletRequest request, int statusCode) {
        String traceId = MDC.get("traceId");
        var errorNotification = ErrorNotification.builder()
                .traceId(traceId)
                .exceptionClass(ex.getClass().getName())
                .message(ex.getMessage())
                .endpoint(request.getRequestURI())
                .statusCode(String.valueOf(statusCode))
                .stackTrace(Arrays.toString(ex.getStackTrace()))
                .occurredAt(LocalDateTime.now())
                .serviceName(environment.getProperty("spring.application.name"))
                .build();
        exceptionPublisher.publishException(errorNotification);
    }
}
