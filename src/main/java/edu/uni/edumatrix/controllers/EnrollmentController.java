package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.EnrollmentDTO;
import edu.uni.edumatrix.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService service;

    @PostMapping
    public ResponseEntity<EnrollmentDTO.Response> enroll(
            @RequestBody @Valid EnrollmentDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(service.enroll(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO.Response>>
    getByStudent(@PathVariable String studentId) {

        return ResponseEntity.ok(
                service.getByStudent(studentId)
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO.Response>>
    getByCourse(@PathVariable String courseId) {

        return ResponseEntity.ok(
                service.getByCourse(courseId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id
    ) {
        service.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
