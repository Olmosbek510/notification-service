package com.vention.examinai.notification_service.controller;

import com.vention.examinai.notification_service.dto.HttpResponse;
import com.vention.examinai.notification_service.enums.ResponseStatus;
import com.vention.examinai.notification_service.service.NotificationThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification-threads")
@RequiredArgsConstructor
public class NotificationThreadController {
    private final NotificationThreadService notificationThreadService;


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
