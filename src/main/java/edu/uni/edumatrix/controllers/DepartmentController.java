package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.DepartmentDTO;
import edu.uni.edumatrix.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDTO.Response> create(
            @RequestBody @Valid DepartmentDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(departmentService.createDepartment(request));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO.Response>> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<DepartmentDTO.Response>> getByFaculty(
            @PathVariable String facultyId
    ) {
        return ResponseEntity.ok(
                departmentService.getDepartmentsByFaculty(facultyId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO.Response> update(
            @PathVariable String id,
            @RequestBody DepartmentDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(
                departmentService.updateDepartment(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        departmentService.deleteDepartment(id);

        return ResponseEntity.noContent().build();
    }
}
