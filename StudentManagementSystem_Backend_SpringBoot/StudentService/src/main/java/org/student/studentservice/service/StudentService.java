package org.student.studentservice.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.student.constants.KafkaConstants;
import org.student.enums.*;
import org.student.studentmanagementdto.*;
import org.student.studentservice.model.Student;
import org.student.studentservice.model.User;
import org.student.studentservice.repository.StudentRepository;
import org.student.studentservice.repository.UserRepository;
import org.student.studentservice.service.feignClient.PaymentNotification;
import org.student.studentservice.service.feignClient.SendStudentVote;


import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class StudentService {

    @Autowired
    private KafkaTemplate<String, StudentNotifyDTO> kafkaStudentRegisterTemplate;

    @Autowired
    private KafkaTemplate<String, Object> kafkaStudentElectionTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final PaymentNotification paymentNotification;

    private final SendStudentVote sendStudentVote;

    public StudentService(PaymentNotification paymentNotification, SendStudentVote sendStudentVote) {
        this.paymentNotification = paymentNotification;
        this.sendStudentVote = sendStudentVote;
    }

    @Autowired
    private StudentRepository studentRepository;

    public ApiResponse<List<Student>> getAllStudentDetails(){
        List<Student> students = studentRepository.findAll();
        String msg = students.isEmpty() ? "No students found" : "Students found";
        int statusValue = students.isEmpty()? HttpStatus.NO_CONTENT.value():HttpStatus.FOUND.value();
        return new ApiResponse<>(msg, students, statusValue);
    }

    public ApiResponse<Student> getStudentById(String studentId) {
        Optional<Student> foundStudent =  studentRepository.findById(studentId);
        String msg = foundStudent.isPresent() ? "Student found" : "No student found with id: " + studentId;
        Student student = foundStudent.orElse(null);
        int statusValue = foundStudent.isPresent()? HttpStatus.FOUND.value():HttpStatus.NOT_FOUND.value();

        return new ApiResponse<>(msg, student, statusValue);
    }

    public ApiResponse<Student> updateStudent(Student student) {
        Student updatedStudent = studentRepository.save(student);
        String msg = "Student data updated successfully";
        return new ApiResponse<>(msg, updatedStudent, HttpStatus.OK.value());
    }

    public ApiResponse<Student> removeStudentById(String id) {
        Optional<Student> foundStudent =  studentRepository.findById(id);
        String msg = foundStudent.isPresent() ? "Student Deleted with id: " + id : "No student found with id: " + id;
        studentRepository.deleteById(id);
        int statusValue = foundStudent.isPresent()? HttpStatus.FOUND.value():HttpStatus.NOT_FOUND.value();
        Student student = foundStudent.orElse(null);
        return new ApiResponse<>(msg, student, statusValue);
    }

    public ApiResponse<Student> registerStudent(StudentDTO studentDTO){
        Student student = new Student();
        student.setName(studentDTO.getFirstName() + " " + studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setAge(studentDTO.getAge());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        student.setAddress(studentDTO.getAddress());
        student.setGovtIdProof(studentDTO.getGovtIdProof());
        student.setVerified(false);
        student.setStudentRole(StudentRole.STUDENT);

        String id = studentDTO.getLastName() + studentDTO.getFirstName().substring(0,2) + new Random().nextInt(1000);
        student.setId(id);

        List<ActivityTypes> list = Arrays.stream(studentDTO.getActivityInterests().split(",")).map(String::strip).map(ActivityTypes::valueOf).toList();
        student.setActivitiesEnrolled(list);

        if(studentDTO.getTestScore()>80){
            student.setStudentType(StudentType.ELITE);
        }
        else if(studentDTO.getTestScore()>50){
            student.setStudentType(StudentType.HONORS);
        }
        else {
            student.setStudentType(StudentType.REGULAR);
        }

        student.setRegistrationDate(LocalDateTime.now());

        String msg = "Student registered successfully";

        studentRepository.save(student);

        kafkaStudentRegisterTemplate.send(KafkaConstants.STUDENT_REGISTER_NOTIFY, new StudentNotifyDTO(student.getId()));


        User user = new User(student.getEmail(), student.getEmail(),passwordEncoder.encode(student.getId()), UserRole.STUDENT);

        userRepository.save(user);

        return new ApiResponse<>(msg, student, HttpStatus.OK.value());
    }

    public String sendPayment(String student_id){
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();

        Optional<Student> student = studentRepository.findById(student_id);
        if(student.isPresent()){
            paymentRequest.setId(student.get().getId());
            paymentRequest.setName(student.get().getName());
            paymentRequest.setEmail(student.get().getEmail());
            paymentRequest.setAmount(100000.0);
            paymentRequest.setStudentType(student.get().getStudentType());

            student.get().setVerified(true);
            studentRepository.save(student.get());

            assert paymentNotification != null;
            PaymentResponseDTO response = paymentNotification.sendPayment(paymentRequest);

            return "Payment response for " + response.getName() + ", " + "Amount: " + response.getAmount() + ", " + "Status: " + response.getStatus();
        }
        else
            return "Student with id " + student_id + " not found";
    }

    public String sendElectionSignal(Boolean electionFlag){
        Map<String,Object> electionMap;
        List<Student> studentList = studentRepository.findAll();
        List<String> topThree = studentList.stream().map(Student::getId).limit(3).toList();

        StudentElectionDTO studentElectionDTO = new StudentElectionDTO(topThree);

        electionMap = new HashMap<>();
        electionMap.put("electionFlag", electionFlag);
        electionMap.put("studentList", studentElectionDTO);

        kafkaStudentElectionTemplate.send(KafkaConstants.ELECTION_SIGNAL, electionMap);
        return electionFlag? "Election signal sent for top 3 students: " + topThree : "Election stop signal sent";
    }

    public String sendVote(String studentId){
        return sendStudentVote.sendVote(studentId);
    }

    public void addRandomStudents () throws Exception {

        StudentDTO studentDTO;
        String filePath = "src/main/resources/Student_Records/student_records.xlsx";

        FileInputStream fis = new FileInputStream(filePath);
        Workbook wb = new XSSFWorkbook(fis);
        Sheet sheet = wb.getSheetAt(0);

        // Skip header row
        boolean firstRow = true;
        for (Row row : sheet) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            studentDTO = new StudentDTO();

            studentDTO.setFirstName(row.getCell(0).getStringCellValue());
            studentDTO.setLastName(row.getCell(1).getStringCellValue());
            studentDTO.setAge((int) row.getCell(2).getNumericCellValue());
            studentDTO.setEmail(row.getCell(3).getStringCellValue());
            studentDTO.setPhoneNumber(row.getCell(4).getStringCellValue());
            studentDTO.setAddress(row.getCell(5).getStringCellValue());
            studentDTO.setActivityInterests(row.getCell(6).getStringCellValue());
            studentDTO.setGovtIdProof(GovtIdProofs.valueOf(row.getCell(7).getStringCellValue()));
            studentDTO.setTestScore(row.getCell(8).getNumericCellValue());

            registerStudent(studentDTO);

        }
    }
}
