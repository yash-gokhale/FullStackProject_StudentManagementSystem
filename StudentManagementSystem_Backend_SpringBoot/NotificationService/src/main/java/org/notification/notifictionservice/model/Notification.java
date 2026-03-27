package org.notification.notifictionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "Notifications", schema = "notification_data")
public class Notification {
    @Id
    private Long id;

    private String studentId;
    private LocalDateTime notificationDate;
    private String remarks;

    public Notification() {}

}
