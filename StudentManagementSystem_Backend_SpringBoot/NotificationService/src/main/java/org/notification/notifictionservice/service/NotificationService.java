package org.notification.notifictionservice.service;

import org.notification.notifictionservice.model.Notification;
import org.notification.notifictionservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.student.studentmanagementdto.ApiResponse;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public ApiResponse<List<Notification>> getAllNotifications(){
        List<Notification> notifications = notificationRepository.findAll(Sort.by(Sort.Direction.DESC, "notificationDate"));
        String msg = notifications.isEmpty() ? "No notifications found" : "Notifications found";
        int statusValue = notifications.isEmpty()? HttpStatus.NO_CONTENT.value():HttpStatus.FOUND.value();
        return new ApiResponse<>(msg, notifications, statusValue);
    }
}
