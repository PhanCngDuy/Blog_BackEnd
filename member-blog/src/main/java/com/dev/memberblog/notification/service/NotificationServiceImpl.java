package com.dev.memberblog.notification.service;

import com.dev.memberblog.notification.dto.NotificationDTO;
import com.dev.memberblog.notification.dto.NotificationMapper;
import com.dev.memberblog.notification.model.Notification;
import com.dev.memberblog.notification.repository.NotificationRepository;
import com.dev.memberblog.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService{
    @Autowired
    private NotificationRepository repository;

    @Override
    public NotificationDTO create(Notification notification) {
        Notification newNotification = repository.save(notification);
        return NotificationMapper.INSTANCE.toDTO(newNotification);
    }

    @Override
    public List<NotificationDTO> findAllByUserId(String userId) {
        return repository.findAllByUser_Id(userId).stream().map(n -> {
            NotificationDTO dto = NotificationMapper.INSTANCE.toDTO(n);
            dto.setRead(n.isRead());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> findAllNotReadByUserId(String userId) {
        return repository.findAllByUserNotRead(userId).stream().map(n -> NotificationMapper.INSTANCE.toDTO(n)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(String id) {
        try{
            repository.deleteById(id);
        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
            return true;
    }

    @Override
    public void readAll(String userId) {
        repository.updateReadAllByUserId(userId);
    }

    @Override
    public void readOne(String notificationId) {
        repository.updateReadById(notificationId);
    }
}
