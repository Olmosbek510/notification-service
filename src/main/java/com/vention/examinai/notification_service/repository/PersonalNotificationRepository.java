package com.vention.examinai.notification_service.repository;

import com.vention.examinai.notification_service.model.PersonalNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Long> {
}