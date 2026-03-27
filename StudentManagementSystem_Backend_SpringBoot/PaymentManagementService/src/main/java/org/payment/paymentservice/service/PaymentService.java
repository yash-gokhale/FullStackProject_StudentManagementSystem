package org.payment.paymentservice.service;

import org.payment.paymentservice.model.Payment;
import org.payment.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.student.enums.StudentType;
import org.student.studentmanagementdto.PaymentRequestDTO;
import org.student.studentmanagementdto.PaymentResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private KafkaTemplate<String, PaymentResponseDTO> kafkaTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO();
        Payment payment = new Payment();

        paymentResponse.setId(paymentRequestDTO.getId());
        paymentResponse.setName(paymentRequestDTO.getName());

        Double amount;

        if(paymentRequestDTO.getStudentType().equals(StudentType.REGULAR)){
            amount = paymentRequestDTO.getAmount();
            paymentResponse.setAmount(amount);
        }
        else if(paymentRequestDTO.getStudentType().equals(StudentType.HONORS)){
            amount = paymentRequestDTO.getAmount() * 0.7;
            paymentResponse.setAmount(amount); // 10% discount
        }
        else if(paymentRequestDTO.getStudentType().equals(StudentType.ELITE)){
            amount = paymentRequestDTO.getAmount() * 0.5;
            paymentResponse.setAmount(amount); // 20% discount
        }

        paymentResponse.setStatus("SUCCESS");

        payment.setPaymentDate(LocalDateTime.now());
        payment.setName(paymentResponse.getName());
        payment.setEmail(paymentRequestDTO.getEmail());
        payment.setPaymentStatus(paymentResponse.getStatus());
        payment.setId(paymentResponse.getId());

        String uuid = UUID.randomUUID().toString();
        payment.setTransactionId(uuid);

        paymentResponse.setTransactionId(uuid);

        paymentRepository.save(payment);

        kafkaTemplate.send("student-payment-topic", paymentResponse);

        return paymentResponse;

    }
}
