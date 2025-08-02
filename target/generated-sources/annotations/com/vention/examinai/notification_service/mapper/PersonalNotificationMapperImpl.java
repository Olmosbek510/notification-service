package com.vention.examinai.notification_service.mapper;

import com.vention.examinai.notification_service.dto.request.TaskNotificationRequest;
import com.vention.examinai.notification_service.model.PersonalNotification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-02T20:58:30+0500",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class PersonalNotificationMapperImpl implements PersonalNotificationMapper {

    @Override
    public PersonalNotification toEntity(TaskNotificationRequest taskNotificationRequest) {
        if ( taskNotificationRequest == null ) {
            return null;
        }

        PersonalNotification.PersonalNotificationBuilder personalNotification = PersonalNotification.builder();

        personalNotification.type( taskNotificationRequest.type() );
        personalNotification.recipientId( taskNotificationRequest.recipientId() );
        personalNotification.senderId( taskNotificationRequest.senderId() );
        personalNotification.subject( taskNotificationRequest.subject() );
        personalNotification.message( taskNotificationRequest.message() );

        return personalNotification.build();
    }
}
