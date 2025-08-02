package com.vention.examinai.notification_service.service.impl;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
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
    public Long save(TaskNotificationRequest taskNotificationRequest) {
        PersonalNotification personalNotification =
                personalNotificationMapper.toEntity(taskNotificationRequest);

        log.info("""
                Personal Notification before persisting:
                %s
                """.formatted(personalNotification));

        PersonalNotification savedNotification = personalNotificationRepository.save(personalNotification);
        return savedNotification.getId();
    }
}
