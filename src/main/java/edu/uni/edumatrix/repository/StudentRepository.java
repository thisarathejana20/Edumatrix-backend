package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository
        extends JpaRepository<Student, String> {
    List<Student> findByDepartmentId(String departmentId);

}
