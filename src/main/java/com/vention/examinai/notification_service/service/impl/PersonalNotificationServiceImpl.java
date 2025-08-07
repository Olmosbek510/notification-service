package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;
import com.vention.examinai.notification_service.dto.response.PersonalNotificationResponse;
import com.vention.examinai.notification_service.mapper.PersonalNotificationMapper;
import com.vention.examinai.notification_service.model.PersonalNotification;
import com.vention.examinai.notification_service.repository.PersonalNotificationRepository;
import com.vention.examinai.notification_service.service.PersonalNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalNotificationServiceImpl implements PersonalNotificationService {
    private final PersonalNotificationRepository personalNotificationRepository;
    private final PersonalNotificationMapper personalNotificationMapper;

    @Override
    public PersonalNotificationResponse save(PersonalNotificationRequest personalNotificationRequest) {
        log.info("Received request to save personal notification for user ID: {}", personalNotificationRequest.recipientId());

        PersonalNotification personalNotification =
                personalNotificationMapper.toEntity(personalNotificationRequest);

        log.debug("Mapped PersonalNotification entity before persistence: {}", personalNotification);

        PersonalNotification savedNotification = personalNotificationRepository.save(personalNotification);

        log.info("Successfully saved personal notification with ID: {}", savedNotification.getId());

        return personalNotificationMapper.toNotificationResponse(savedNotification);
    }

}
