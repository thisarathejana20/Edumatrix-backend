package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.CourseAssignmentDTO;
import edu.uni.edumatrix.service.CourseAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-assignment")
@RequiredArgsConstructor
public class CourseAssignmentController {

    private final CourseAssignmentService service;

    @PostMapping
    public ResponseEntity<CourseAssignmentDTO.Response> assign(
            @RequestBody @Valid CourseAssignmentDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(service.assignCourse(request));
    }

    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<List<CourseAssignmentDTO.Response>>
    getByLecturer(@PathVariable String lecturerId) {

        return ResponseEntity.ok(
                service.getAssignmentsByLecturer(lecturerId)
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseAssignmentDTO.Response>>
    getByCourse(@PathVariable String courseId) {

        return ResponseEntity.ok(
                service.getAssignmentsByCourse(courseId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
