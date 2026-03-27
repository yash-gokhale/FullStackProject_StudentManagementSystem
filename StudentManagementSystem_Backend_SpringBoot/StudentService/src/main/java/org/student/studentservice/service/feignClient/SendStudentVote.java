package org.student.studentservice.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.student.studentmanagementdto.PaymentRequestDTO;
import org.student.studentmanagementdto.PaymentResponseDTO;

@FeignClient(
        name = "student-council-service",
        url = "http://localhost:8084"
)
public interface SendStudentVote {

    @PostMapping("/council/vote/{studentId}")
    String sendVote(@PathVariable("studentId") String studentId);
}
