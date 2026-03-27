package org.student.studentmanagementdto;

import org.student.enums.ActivityTypes;
import org.student.enums.GovtIdProofs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDTO {
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String address;
    private String activityInterests;
    private GovtIdProofs govtIdProof;
    private Double testScore;

    public StudentDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivityInterests() {
        return activityInterests;
    }

    public void setActivityInterests(String activityInterests) {
        this.activityInterests = activityInterests;
    }

    public GovtIdProofs getGovtIdProof() {
        return govtIdProof;
    }

    public void setGovtIdProof(GovtIdProofs govtIdProof) {
        this.govtIdProof = govtIdProof;
    }

    public Double getTestScore() {
        return testScore;
    }

    public void setTestScore(Double testScore) {
        this.testScore = testScore;
    }
}


