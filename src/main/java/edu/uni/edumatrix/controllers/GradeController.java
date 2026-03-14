package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.GradeDTO;
import edu.uni.edumatrix.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService service;

    @PostMapping
    public ResponseEntity<GradeDTO.Response> publish(
            @RequestBody @Valid GradeDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(service.publishGrade(request));
    }

    @GetMapping("/enrollment/{enrollmentId}")
    public ResponseEntity<GradeDTO.Response> get(
            @PathVariable String enrollmentId
    ) {
        return ResponseEntity.ok(
                service.getByEnrollment(enrollmentId)
        );
    }
}
