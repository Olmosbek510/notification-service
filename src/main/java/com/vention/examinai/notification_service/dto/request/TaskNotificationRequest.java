package com.vention.examinai.notification_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskNotificationRequest(
        @NotBlank(message = "Type is required")
        String type,

        @NotBlank(message = "Recipient ID is required")
        String recipientId,

        @NotBlank(message = "Sender ID is required")
        String senderId,

        @NotBlank(message = "Subject is required")
        @Size(max = 255, message = "Subject must be less than 255 characters")
        String subject,

        @NotBlank(message = "Message is required")
        String message
) {
}
