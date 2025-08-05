package com.vention.examinai.notification_service.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationEmailDto {
    String to;
    String username;
    String confirmationLink;
}
