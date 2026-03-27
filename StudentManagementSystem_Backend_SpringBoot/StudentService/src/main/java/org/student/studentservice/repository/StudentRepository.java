package org.student.studentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.student.studentservice.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("SELECT s.id FROM Student s WHERE s.email = :email")
    String findIdByEmail(@Param("email") String email);
}
