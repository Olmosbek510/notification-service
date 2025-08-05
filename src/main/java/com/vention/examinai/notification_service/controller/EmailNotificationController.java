package com.vention.examinai.notification_service.controller;

import com.vention.examinai.notification_service.dto.email.ConfirmationEmailDto;
import com.vention.examinai.notification_service.enums.EmailExchange;
import com.vention.examinai.notification_service.enums.EmailRoutingKey;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class EmailNotificationController {

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public ResponseEntity<String> sendTestMessage(@RequestParam String to,
                                                  @RequestParam String username,
                                                  @RequestParam String link) {
        ConfirmationEmailDto dto = new ConfirmationEmailDto(to, username, link);

        rabbitTemplate.convertAndSend(
                EmailExchange.DIRECT_EMAIL.getValue(),
                EmailRoutingKey.CONFIRMATION_EMAIL.getValue(),
                dto
        );

        return ResponseEntity.ok("DTO was sent");
    }
}
