package com.vention.examinai.notification_service.controller;

import com.vention.examinai.notification_service.dto.HttpResponse;
import com.vention.examinai.notification_service.enums.ResponseStatus;
import com.vention.examinai.notification_service.service.NotificationThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification-threads")
@RequiredArgsConstructor
public class NotificationThreadController {
    private final NotificationThreadService notificationThreadService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/start/{id}")
    public ResponseEntity<HttpResponse> startListener(@PathVariable String id) {
        String response = notificationThreadService.start(id);
        return ResponseEntity.ok(
            HttpResponse.builder()
                .statusCode(ResponseStatus.OK.getStatusCode())
                .description(ResponseStatus.OK.getDescription())
                .data(response)
                .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stop/{id}")
    public ResponseEntity<HttpResponse> stopListener(@PathVariable String id) {
        String response = notificationThreadService.stop(id);
        return ResponseEntity.ok(
            HttpResponse.builder()
                .statusCode(ResponseStatus.OK.getStatusCode())
                .description(ResponseStatus.OK.getDescription())
                .data(response)
                .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<HttpResponse> getStatuses() {
        Map<String, Boolean> threadServiceStatuses = notificationThreadService.getStatuses();
        return ResponseEntity.ok(
            HttpResponse.builder()
                .statusCode(ResponseStatus.OK.getStatusCode())
                .description(ResponseStatus.OK.getDescription())
                .data(threadServiceStatuses)
                .build()
        );
    }
}
