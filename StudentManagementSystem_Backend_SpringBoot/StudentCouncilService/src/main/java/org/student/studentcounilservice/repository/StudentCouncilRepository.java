package org.student.studentcounilservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.student.studentcounilservice.model.StudentCouncil;

@Repository
public interface StudentCouncilRepository extends JpaRepository<StudentCouncil,String> {
}
