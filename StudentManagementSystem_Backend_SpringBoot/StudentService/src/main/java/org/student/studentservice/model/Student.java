package org.student.studentservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.student.enums.ActivityTypes;
import org.student.enums.GovtIdProofs;
import org.student.enums.StudentRole;
import org.student.enums.StudentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Setter
@Table(name = "Students", schema = "student_data")
public class Student {

    @Id
    @Column(name = "Student_Id")
    private String id;

    @Column(name = "Name")
    private String name;

    @Email
    @Column(name = "Email", unique = true, nullable = false)
    private String email;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "Mobile")
    private String phoneNumber;

    @Column(name = "Address")
    private String address;

    @Column(name = "Activities_Enrolled")
    private String activitiesEnrolled;

    @Column(name = "Student_Role")
    @Enumerated(EnumType.STRING)
    private StudentRole studentRole;

    @Column(name = "Govt_Id_Proof")
    @Enumerated(EnumType.STRING)
    private GovtIdProofs govtIdProof;

    @Column(name = "Student_Type")
    @Enumerated(EnumType.STRING)
    private StudentType studentType;

    @Column(name = "Verified")
    private Boolean verified;

    @Column(name = "Registration_Date")
    private LocalDateTime registrationDate;


    public Student() {}

    // Getter converts String -> List<ActivityType>
    public List<ActivityTypes> getActivitiesEnrolled() {
        if (activitiesEnrolled == null || activitiesEnrolled.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(activitiesEnrolled.split(","))
                .map(ActivityTypes::valueOf) // Convert String to Enum
                .collect(Collectors.toList());
    }

    // Setter converts List<ActivityType> -> String
    public void setActivitiesEnrolled(List<ActivityTypes> list) {
        if (list == null || list.isEmpty()) {
            this.activitiesEnrolled = "";
        } else {
            this.activitiesEnrolled = list.stream()
                    .map(ActivityTypes::name) // Enum -> String
                    .collect(Collectors.joining(","));
        }
    }
}
