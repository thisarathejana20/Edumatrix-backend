package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.EnrollmentDTO;
import edu.uni.edumatrix.model.Course;
import edu.uni.edumatrix.model.Enrollment;
import edu.uni.edumatrix.model.Student;
import edu.uni.edumatrix.repository.CourseRepository;
import edu.uni.edumatrix.repository.EnrollmentRepository;
import edu.uni.edumatrix.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentDTO.Response enroll(
            EnrollmentDTO.CreateRequest request
    ) {

        Student student = studentRepository.findById(
                request.getStudentId()
        ).orElseThrow(() ->
                new RuntimeException("Student not found"));

        Course course = courseRepository.findById(
                request.getCourseId()
        ).orElseThrow(() ->
                new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .id(UUID.randomUUID().toString())
                .student(student)
                .course(course)
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .attemptNumber(request.getAttemptNumber())
                .status(request.getStatus())
                .enrolledDate(LocalDate.now())
                .build();

        repository.save(enrollment);

        return map(enrollment);
    }

    public List<EnrollmentDTO.Response>
    getByStudent(String studentId) {

        return repository.findByStudentId(studentId)
                .stream()
                .map(this::map)
                .toList();
    }

    public List<EnrollmentDTO.Response>
    getByCourse(String courseId) {

        return repository.findByCourseId(courseId)
                .stream()
                .map(this::map)
                .toList();
    }

    public void deleteEnrollment(String id) {

        repository.deleteById(id);
    }

    private EnrollmentDTO.Response map(Enrollment e) {

        return new EnrollmentDTO.Response(
                e.getId(),
                e.getStudent().getId(),
                e.getStudent().getUser().getFullName(),
                e.getCourse().getId(),
                e.getCourse().getName(),
                e.getAcademicYear(),
                e.getSemester(),
                e.getAttemptNumber(),
                e.getStatus(),
                e.getEnrolledDate()
        );
    }
}
