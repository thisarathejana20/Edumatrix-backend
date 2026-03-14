package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.CourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseAssignmentRepository
        extends JpaRepository<CourseAssignment, String> {

    List<CourseAssignment> findByLecturerId(String lecturerId);

    List<CourseAssignment> findByCourseId(String courseId);

}
