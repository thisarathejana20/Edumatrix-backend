package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.CourseDTO;
import edu.uni.edumatrix.model.Course;
import edu.uni.edumatrix.model.Department;
import edu.uni.edumatrix.repository.CourseRepository;
import edu.uni.edumatrix.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class  CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    public CourseDTO.Response createCourse(CourseDTO.CreateRequest request) {

        if (courseRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new RuntimeException("Course code already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Course course = Course.builder()
                .id(UUID.randomUUID().toString())
                .department(department)
                .name(request.getName())
                .code(request.getCode())
                .credits(request.getCredits())
                .semester(request.getSemester())
                .description(request.getDescription())
                .build();

        courseRepository.save(course);

        return map(course);
    }

    public List<CourseDTO.Response> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<CourseDTO.Response> getCoursesByDepartment(String departmentId) {

        return courseRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::map)
                .toList();
    }

    public CourseDTO.Response updateCourse(String id, CourseDTO.CreateRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        course.setDepartment(department);
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredits(request.getCredits());
        course.setSemester(request.getSemester());
        course.setDescription(request.getDescription());

        courseRepository.save(course);

        return map(course);
    }

    public void deleteCourse(String id) {

        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }

        courseRepository.deleteById(id);
    }

    private CourseDTO.Response map(Course course) {

        return new CourseDTO.Response(
                course.getId(),
                course.getDepartment().getId(),
                course.getDepartment().getName(),
                course.getName(),
                course.getCode(),
                course.getCredits(),
                course.getSemester(),
                course.getDescription()
        );
    }
}
