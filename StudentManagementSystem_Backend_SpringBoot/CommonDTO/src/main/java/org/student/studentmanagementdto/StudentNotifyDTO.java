package org.student.studentmanagementdto;

public class StudentNotifyDTO {
    private String studentId;

    public StudentNotifyDTO() {}

    public StudentNotifyDTO(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
