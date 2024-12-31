package com.dev.memberblog.notification.dto;

import com.dev.memberblog.notification.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);
    NotificationDTO toDTO(Notification entity);
}
