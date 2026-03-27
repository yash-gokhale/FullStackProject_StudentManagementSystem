package org.student.studentservice.service.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.student.studentmanagementdto.PaymentRequestDTO;
import org.student.studentmanagementdto.PaymentResponseDTO;

@FeignClient(
        name = "payment-service",
        url = "http://localhost:8082"
)
public interface PaymentNotification {

    @PostMapping("/api/send-payment")
    PaymentResponseDTO sendPayment(@RequestBody PaymentRequestDTO paymentRequestDTO);
}
