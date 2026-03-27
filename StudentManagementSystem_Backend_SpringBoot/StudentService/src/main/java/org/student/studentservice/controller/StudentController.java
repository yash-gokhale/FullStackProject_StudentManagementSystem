package org.student.studentservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.student.studentmanagementdto.ApiResponse;
import org.student.studentmanagementdto.StudentDTO;
import org.student.studentservice.model.Student;
import org.student.studentservice.security.JWTUtil;
import org.student.studentservice.service.StudentService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Student>>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudentDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable("id") String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Student>> addStudent(@RequestBody StudentDTO student) {
        return ResponseEntity.ok(studentService.registerStudent(student));
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<String> makePayment(@PathVariable("id") String student_id) {
        return ResponseEntity.ok(studentService.sendPayment(student_id));
    }

    @PutMapping("/election/send-signal/start")
    public ResponseEntity<String> signalStartElection(){
    return ResponseEntity.ok(studentService.sendElectionSignal(true));
    }

    @PutMapping("/election/send-signal/stop")
    public ResponseEntity<String> signalStopElection(){
        return ResponseEntity.ok(studentService.sendElectionSignal(false));
    }

    @PostMapping("/election/send-vote/{studentId}")
    public ResponseEntity<String> sendVote(@PathVariable("studentId") String studentId) {
        return ResponseEntity.ok(studentService.sendVote(studentId));
    }

    @PostMapping("/add-random")
    public ResponseEntity<String> addRandomStudents() {
        try {
            studentService.addRandomStudents();
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding random students: " + e.getMessage());
        }
        return ResponseEntity.ok("Added Students");
    }



    @DeleteMapping("/remove/{id}")
    public ResponseEntity<ApiResponse<Student>> removeStudent(@PathVariable("id") String id) {
        return ResponseEntity.ok(studentService.removeStudentById(id));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Student>> updateStudent( @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(student));
    }

}


