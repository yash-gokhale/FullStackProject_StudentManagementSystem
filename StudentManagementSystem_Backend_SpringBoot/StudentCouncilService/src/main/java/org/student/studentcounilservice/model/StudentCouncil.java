package org.student.studentcounilservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "Student_Council", schema = "student_council_data")
public class StudentCouncil {
    @Id
    private String event_id;
    private String event_name;
    private String president;
    private String vice_president;
    private String secretary;
    private LocalDateTime event_date;

        public StudentCouncil() {
        }
}
