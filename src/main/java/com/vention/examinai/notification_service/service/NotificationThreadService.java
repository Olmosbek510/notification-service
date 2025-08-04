package com.vention.examinai.notification_service.service;

import java.util.Map;

public interface NotificationThreadService {
    String start(String listenerId);
    String stop(String listenerId);
    Map<String, Boolean> getStatuses();
}
