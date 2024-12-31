package com.dev.memberblog.notification.controller;

import com.dev.memberblog.common.helper.ResponseHelper;
import com.dev.memberblog.notification.dto.NotificationDTO;
import com.dev.memberblog.notification.model.Notification;
import com.dev.memberblog.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @GetMapping("/{userId}")
    public Object getAllNotification(@PathVariable String userId){
        return ResponseHelper.getResponse(service.findAllByUserId(userId),HttpStatus.OK);
    }
    @GetMapping("/not-read/{userId}")
    public Object getNotificationNotRead(@PathVariable String userId){
        return ResponseHelper.getResponse(service.findAllNotReadByUserId(userId),HttpStatus.OK);
    }

    @PostMapping("/read-all/{userId}")
    public Object readAllNotification(@PathVariable String userId){
        service.readAll(userId);
        return ResponseHelper.getResponse("Read All Notification",HttpStatus.OK);
    }

    @PostMapping("/read/{notificationId}")
    public Object readNotification(@PathVariable String notificationId){
        service.readOne(notificationId);
        return ResponseHelper.getResponse("Read All Notification",HttpStatus.OK);
    }

    @PostMapping("/delete/{notificationId}")
    public Object deleteOneNotification(@PathVariable String notificationId){
        boolean result = service.deleteById(notificationId);
        if(!result){
            return ResponseHelper.getErrorResponse("Delete failure.",HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse("Deleted",HttpStatus.OK);
    }


}
