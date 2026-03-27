package org.notification.notifictionservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.notification.notifictionservice.model.Notification;
import org.notification.notifictionservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.student.constants.KafkaConstants;
import org.student.studentmanagementdto.PaymentResponseDTO;
import org.student.studentmanagementdto.StudentElectionDTO;
import org.student.studentmanagementdto.StudentNotifyDTO;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Component
public class TopicConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "student-payment-topic", groupId = "notification-group")
    public void receiveNotification(PaymentResponseDTO paymentResponseDTO) {
        Notification notification = new Notification();
        notification.setStudentId(paymentResponseDTO.getId());
        notification.setNotificationDate(LocalDateTime.now());

        String remarks = "Payment received for student:" + notification.getStudentId() + " with transactionId:" + paymentResponseDTO.getTransactionId();
        notification.setRemarks(remarks);

        Long notificationId = generateNotificationId();
        notification.setId(notificationId);

        notificationRepository.save(notification);
    }

    @KafkaListener(topics = "student-register-topic", groupId = "notification-group")
    public void receiveRegistrationNotification(StudentNotifyDTO studentNotifyDTO) {
        Notification notification = new Notification();
        notification.setStudentId(studentNotifyDTO.getStudentId());
        notification.setNotificationDate(LocalDateTime.now());

        String remarks = "New student registered:" + studentNotifyDTO.getStudentId();
        notification.setRemarks(remarks);

        Long notificationId = generateNotificationId();
        notification.setId(notificationId);

        notificationRepository.save(notification);
    }

    @KafkaListener(topics = KafkaConstants.ELECTION_NOTIFY, groupId = "notification-group")
    public void receiveElectionNotification(Map<String,Object> electionNotify) {
        Notification notification = new Notification();
        StudentElectionDTO dto = objectMapper.convertValue(electionNotify.get("studentList"), StudentElectionDTO.class);
        Boolean electionFlag = (Boolean) electionNotify.get("electionFlag");
        if (Boolean.TRUE.equals(electionFlag)) {

            for (String studentId : dto.getStudentIdList()) {
                notification.setNotificationDate(LocalDateTime.now());
                notification.setStudentId(studentId);

                Long notificationId = generateNotificationId();
                notification.setId(notificationId);

                String remarks = "Student with id:" + studentId + " is participating in the election";
                notification.setRemarks(remarks);

                notificationRepository.save(notification);
            }
        }
        else{
            notification.setId(generateNotificationId());
            notification.setNotificationDate(LocalDateTime.now());

            String remarks = "Election has been stopped";
            notification.setRemarks(remarks);
            notification.setStudentId(null);

            notificationRepository.save(notification);
        }
    }

        @KafkaListener(topics = KafkaConstants.ELECTION_VOTE_SENT, groupId = "notification-group")
        public void receiveElectionVoteNotification(String studentId) {
            Notification notification = new Notification();
            notification.setNotificationDate(LocalDateTime.now());
            notification.setStudentId(studentId);

            Long notificationId = generateNotificationId();
            notification.setId(notificationId);

            String remarks = "Vote received for student with id:" + studentId;
            notification.setRemarks(remarks);

            notificationRepository.save(notification);

    }

    private Long generateNotificationId() {
        return new Random().nextLong(100000);
    }
}
