package org.notification.notifictionservice.controller;


import org.notification.notifictionservice.model.Notification;
import org.notification.notifictionservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.student.studentmanagementdto.ApiResponse;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    public NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(){
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}
