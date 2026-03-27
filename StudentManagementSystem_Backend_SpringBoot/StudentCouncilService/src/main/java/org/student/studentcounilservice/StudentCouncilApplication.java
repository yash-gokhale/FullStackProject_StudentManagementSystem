package org.student.studentcounilservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentCouncilApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentCouncilApplication.class, args);
    }

}
