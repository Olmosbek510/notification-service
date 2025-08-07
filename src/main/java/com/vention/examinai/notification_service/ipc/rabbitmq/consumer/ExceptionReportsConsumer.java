package com.vention.examinai.notification_service.ipc.rabbitmq.consumer;

import com.vention.examinai.notification_service.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ExceptionReportsConsumer {

    private final NotificationSenderService notificationSenderService;

    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.exceptions-text}")
    public void receiveTextReport(@Payload String textReport) {
        // todo: fetch the proper admins emails then send the report to them
        notificationSenderService.sendTextEmail("jw017306@gmail.com", "Daily Exceptions Report (Text)", textReport);
    }

    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.exceptions-csv}")
    public void receiveExcelReport(Message message) {

        byte[] excelBytes = message.getBody();

        String fileName = "exceptions_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

        notificationSenderService.sendExcelEmail(
                "jw017306@gmail.com",
                "Periodic Exceptions Report (Excel)",
                fileName,
                excelBytes
        );
    }

}
