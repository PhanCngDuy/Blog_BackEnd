package com.dev.memberblog.notification.service;

import com.dev.memberblog.notification.dto.NotificationDTO;
import com.dev.memberblog.notification.model.Notification;
import com.dev.memberblog.user.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationService {

    NotificationDTO create(Notification notification);

   List<NotificationDTO> findAllByUserId(String userId);

   List<NotificationDTO> findAllNotReadByUserId(String userId);

    boolean deleteById(String id);

    void readAll(String userId);

    void readOne(String notificationId);
}
