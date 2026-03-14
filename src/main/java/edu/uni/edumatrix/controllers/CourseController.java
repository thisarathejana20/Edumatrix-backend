package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.CourseDTO;
import edu.uni.edumatrix.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO.Response> create(
            @RequestBody @Valid CourseDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO.Response>> getAll() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CourseDTO.Response>> getByDepartment(
            @PathVariable String departmentId
    ) {
        return ResponseEntity.ok(
                courseService.getCoursesByDepartment(departmentId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO.Response> update(
            @PathVariable String id,
            @RequestBody CourseDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(
                courseService.updateCourse(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }
}
