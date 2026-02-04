package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByFaculty_Id(Long facultyId);

    boolean existsByFaculty_IdAndNameIgnoreCase(Long facultyId, String name);
    boolean existsByFaculty_IdAndNameIgnoreCaseAndIdNot(Long facultyId, String name, Long id);
}
