package com.vention.examinai.notification_service.dto.response;

public record PersonalNotificationResponse(
        Long id,
        String type,
        String recipientId,
        String senderId,
        String subject,
        String message
) {
}
