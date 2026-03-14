package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.StudentDTO;
import edu.uni.edumatrix.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @PostMapping
    public ResponseEntity<StudentDTO.Response> create(
            @RequestBody @Valid StudentDTO.CreateRequest request,
            @RequestParam boolean hasSystemAccess
    ) {
        return ResponseEntity.ok(service.createStudent(request, hasSystemAccess));
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO.Response>> getAll() {
        return ResponseEntity.ok(service.getAllStudents());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentDTO.Response>>
    getByDepartment(@PathVariable String departmentId) {

        return ResponseEntity.ok(
                service.getStudentsByDepartment(departmentId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO.Response> update(
            @PathVariable String id,
            @RequestBody StudentDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(
                service.updateStudent(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
