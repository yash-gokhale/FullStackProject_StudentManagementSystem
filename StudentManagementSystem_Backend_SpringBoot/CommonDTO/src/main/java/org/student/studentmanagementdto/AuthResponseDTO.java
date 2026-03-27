package org.student.studentmanagementdto;

import org.student.enums.UserRole;

public class AuthResponseDTO {
    private String token;
    private UserRole role;
    private String studentId;

    public AuthResponseDTO(String token, UserRole role,  String studentId) {
        this.token = token;
        this.role = role;
        this.studentId = studentId;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
