package org.student.studentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.student.enums.UserRole;
import org.student.studentmanagementdto.ApiResponse;
import org.student.studentmanagementdto.AuthResponseDTO;
import org.student.studentmanagementdto.UserLoginDTO;
import org.student.studentmanagementdto.UserRegisterDTO;
import org.student.studentservice.model.Student;
import org.student.studentservice.model.User;
import org.student.studentservice.repository.StudentRepository;
import org.student.studentservice.repository.UserRepository;
import org.student.studentservice.security.JWTUtil;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JWTUtil  jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody UserRegisterDTO userDTO) {
        List<User> admin = userRepository.findByRole(UserRole.ADMIN);
        if (!admin.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>("Admin account already exists", null, HttpStatus.CONFLICT.value()));
        }
        User foundUser = userRepository.findByEmail(userDTO.getUsername()).orElse(null);
        if (foundUser != null) {
            return ResponseEntity.ok(new ApiResponse<>("User already exists", null, HttpStatus.CONFLICT.value()));
        }
        User user = new User();
        user.setName(userDTO.getUsername());
        user.setEmail(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully", user, HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody UserLoginDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(new ApiResponse<>("User does not exist", new AuthResponseDTO(null, null, null), HttpStatus.NOT_FOUND.value()));
        }
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(new ApiResponse<>("Incorrect password", new AuthResponseDTO(null, null, null), HttpStatus.UNAUTHORIZED.value()));
        }
        String token = jwtUtil.generateToken(userDTO.getEmail());


           String studentId = studentRepository.findIdByEmail(userDTO.getEmail());

        return ResponseEntity.ok(new ApiResponse<>("User logged in successfully", new AuthResponseDTO(token, user.getRole(), studentId), HttpStatus.OK.value()));
    }
}
