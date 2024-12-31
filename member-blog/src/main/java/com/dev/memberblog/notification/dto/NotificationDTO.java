package com.dev.memberblog.notification.dto;

import com.dev.memberblog.notification.model.NotificationType;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class NotificationDTO {
    private String id;
    private String path;
    private LocalDateTime createAt;
    private String body;
    private String content;
    private UserDetailsDTO sender;
    private NotificationType type;
    private boolean isRead;
}
