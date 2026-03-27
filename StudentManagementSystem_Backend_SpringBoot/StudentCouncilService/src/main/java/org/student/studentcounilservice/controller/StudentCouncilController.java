package org.student.studentcounilservice.controller;

import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.student.studentcounilservice.service.StudentCouncilService;

@RestController
@RequestMapping("/council")
public class StudentCouncilController {

    @Autowired
    private StudentCouncilService studentCouncilService;

    @PostMapping("/vote/{studentId}")
    public String receiveVote(@PathVariable("studentId") String id) {
        return studentCouncilService.processVotes(id);
    }

}
