package com.vention.examinai.notification_service.mapper;

import com.vention.examinai.notification_service.dto.request.PersonalNotificationRequest;
import com.vention.examinai.notification_service.dto.response.PersonalNotificationResponse;
import com.vention.examinai.notification_service.model.PersonalNotification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonalNotificationMapper {
    PersonalNotification toEntity(PersonalNotificationRequest personalNotificationRequest);
    PersonalNotificationResponse toNotificationResponse(PersonalNotification notification);
}
