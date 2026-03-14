package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    boolean existsByCodeIgnoreCase(String code);

    List<Course> findByDepartmentId(String departmentId);

}
