package org.student.studentmanagementdto;

import java.util.List;

public class StudentElectionDTO {
    private List<String> studentIdList;

    public StudentElectionDTO() {}

    public StudentElectionDTO(List<String> studentIdList) {
        this.studentIdList = studentIdList;
    }

    public List<String> getStudentIdList() {
        return studentIdList;
    }

    public void setStudentIdList(List<String> studentIdList) {
        this.studentIdList = studentIdList;
    }
}
