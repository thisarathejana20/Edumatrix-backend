package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, String> {
    boolean existsByFacultyIdAndNameIgnoreCase(String facultyId, String name);

    List<Department> findByFacultyId(String facultyId);
}
