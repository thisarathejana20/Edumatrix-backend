package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.CourseAssignmentDTO;
import edu.uni.edumatrix.model.Course;
import edu.uni.edumatrix.model.CourseAssignment;
import edu.uni.edumatrix.model.Lecturer;
import edu.uni.edumatrix.repository.CourseAssignmentRepository;
import edu.uni.edumatrix.repository.CourseRepository;
import edu.uni.edumatrix.repository.LecturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {

    private final CourseAssignmentRepository repository;
    private final LecturerRepository lecturerRepository;
    private final CourseRepository courseRepository;

    public CourseAssignmentDTO.Response assignCourse(
            CourseAssignmentDTO.CreateRequest request
    ) {

        Lecturer lecturer = lecturerRepository.findById(
                request.getLecturerId()
        ).orElseThrow(() ->
                new RuntimeException("Lecturer not found"));

        Course course = courseRepository.findById(
                request.getCourseId()
        ).orElseThrow(() ->
                new RuntimeException("Course not found"));

        CourseAssignment assignment = CourseAssignment.builder()
                .id(UUID.randomUUID().toString())
                .lecturer(lecturer)
                .course(course)
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .assignedDate(LocalDate.now())
                .role(request.getRole())
                .build();

        repository.save(assignment);

        return map(assignment);
    }

    public List<CourseAssignmentDTO.Response>
    getAssignmentsByLecturer(String lecturerId) {

        return repository.findByLecturerId(lecturerId)
                .stream()
                .map(this::map)
                .toList();
    }

    public List<CourseAssignmentDTO.Response>
    getAssignmentsByCourse(String courseId) {

        return repository.findByCourseId(courseId)
                .stream()
                .map(this::map)
                .toList();
    }

    public void deleteAssignment(String id) {

        repository.deleteById(id);
    }

    private CourseAssignmentDTO.Response map(
            CourseAssignment a
    ) {

        return new CourseAssignmentDTO.Response(
                a.getId(),
                a.getLecturer().getId(),
                a.getLecturer().getUser().getFullName(),
                a.getCourse().getId(),
                a.getCourse().getName(),
                a.getAcademicYear(),
                a.getSemester(),
                a.getAssignedDate(),
                a.getRole()
        );
    }
}
