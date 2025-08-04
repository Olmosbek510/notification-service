package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.enums.ResponseStatus;
import com.vention.examinai.notification_service.exception.ApiException;
import com.vention.examinai.notification_service.service.NotificationThreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationThreadServiceImpl implements NotificationThreadService {
    private final RabbitListenerEndpointRegistry registry;

    @Override
    public String start(String listenerId) {
        var container = registry.getListenerContainer(listenerId);

        if (container == null)
            throw new ApiException(ResponseStatus.LISTENER_CONTAINER_NOT_FOUND);
        if (container.isRunning())
            throw new ApiException(ResponseStatus.LISTENER_CONTAINER_ALREADY_STARTED);

        container.start();
        return "Started listener container with id: " + listenerId;
    }

    @Override
    public String stop(String listenerId) {
        var container = registry.getListenerContainer(listenerId);

        if (container == null)
            throw new ApiException(ResponseStatus.LISTENER_CONTAINER_NOT_FOUND);
        if (!container.isRunning())
            throw new ApiException(ResponseStatus.LISTENER_CONTAINER_ALREADY_STOPPED);

        container.stop();
        return "Stopped listener container with id: " + listenerId;
    }

    @Override
    public Map<String, Boolean> getStatuses() {
        Map<String, Boolean> status = new LinkedHashMap<>();
        registry.getListenerContainerIds()
            .forEach(id -> status.put(id, registry.getListenerContainer(id).isRunning()));
        return status;
    }
}
