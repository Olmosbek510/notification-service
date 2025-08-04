package com.vention.examinai.notification_service.mapper;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.model.PersonalNotification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonalNotificationMapper {
    PersonalNotification toEntity(TaskNotificationRequest taskNotificationRequest);
}
