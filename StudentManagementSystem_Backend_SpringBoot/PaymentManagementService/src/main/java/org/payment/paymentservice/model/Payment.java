package org.payment.paymentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "Payments", schema = "payment_data")
public class Payment {
    @Id
    private String transactionId;

    @Column(name = "Student_Id")
    private String id;
    private String name;
    private String email;

    @Column(name = "Payment_Status")
    private String paymentStatus;

    private LocalDateTime paymentDate;

    public Payment() {}
}
