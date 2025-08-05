package com.vention.examinai.notification_service.ipc.rabbitmq.consumer;

import com.vention.examinai.notification_service.service.NotificationSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;

@Component
@RequiredArgsConstructor
public class ExceptionReportsConsumer {

    private final NotificationSenderService notificationSenderService;
    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.exceptions-text}")
    public void receiveTextReport(@Payload String textReport) {
        notificationSenderService.sendTextEmail("olmosbekorazboyev@gmail.com", "Daily Exceptions Report (Text)", textReport);
    }

    @RabbitListener(queues = "${spring.rabbitmq.notification.queues.exceptions-csv}")
    public void receiveExcelReport(Message message) {

        byte[] excelBytes = message.getBody();

        notificationSenderService.sendExcelEmail(
                "olmosbekorazboyev@gmail.com",
                "Periodic Exceptions Report (Excel)",
                "exceptions_report.xlsx",
                excelBytes
        );
    }

}
