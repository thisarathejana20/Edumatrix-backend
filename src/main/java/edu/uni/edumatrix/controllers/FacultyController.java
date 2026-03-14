package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.FacultyDTO;
import edu.uni.edumatrix.service.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<FacultyDTO.Response> createFaculty(
            @RequestBody @Valid FacultyDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(facultyService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<FacultyDTO.Response>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyDTO.Response> getFaculty(@PathVariable String id) {
        return ResponseEntity.ok(facultyService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacultyDTO.Response> updateFaculty(
            @PathVariable String id,
            @RequestBody FacultyDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(facultyService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable String id) {

        facultyService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
