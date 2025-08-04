package com.vention.examinai.notification_service.dto.response;

public record ErrorResponse(
        Integer statusCode,
        String message
) {
}
