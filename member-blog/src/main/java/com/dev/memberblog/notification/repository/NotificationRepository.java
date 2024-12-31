package com.dev.memberblog.notification.repository;

import com.dev.memberblog.notification.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,String> {
    @Query("SELECT n FROM Notification n WHERE n.receiver.id = ?1 ORDER BY n.createAt desc")
    List<Notification> findAllByUser_Id(String userId);

    @Query("SELECT n FROM Notification n WHERE n.receiver.id = ?1 AND n.isRead = false ORDER BY n.createAt desc")
    List<Notification> findAllByUserNotRead(String userId);
    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.receiver.id = ?1 AND n.isRead = false ")
    void updateReadAllByUserId(String userId);
    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = ?1 AND n.isRead = false ")
    void updateReadById(String notificationId);
}
