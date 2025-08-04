package com.vention.examinai.notification_service.dto.email;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationSuccessEmailDto {
    String to;
    String username;
}