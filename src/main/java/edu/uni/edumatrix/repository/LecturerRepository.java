package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {
    List<Lecturer> findByDepartmentId(String departmentId);

}
