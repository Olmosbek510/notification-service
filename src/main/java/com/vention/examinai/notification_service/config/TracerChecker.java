package com.vention.examinai.notification_service.config;

import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TracerChecker {
    private final Tracer tracer;

    @PostConstruct
    public void checkTracer() {
        System.out.println("Tracer bean: " + tracer);
    }
}
