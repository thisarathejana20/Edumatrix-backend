package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.LecturerDTO;
import edu.uni.edumatrix.service.LecturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping
    public ResponseEntity<LecturerDTO.Response> create(
            @RequestBody @Valid LecturerDTO.CreateRequest request,
            @RequestParam boolean hasSystemAccess
    ) {
        return ResponseEntity.ok(lecturerService.createLecturer(request, hasSystemAccess));
    }

    @GetMapping
    public ResponseEntity<List<LecturerDTO.Response>> getAll() {
        return ResponseEntity.ok(lecturerService.getAllLecturers());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<LecturerDTO.Response>> getByDepartment(
            @PathVariable String departmentId
    ) {
        return ResponseEntity.ok(
                lecturerService.getLecturersByDepartment(departmentId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LecturerDTO.Response> update(
            @PathVariable String id,
            @RequestBody LecturerDTO.CreateRequest request
    ) {
        return ResponseEntity.ok(
                lecturerService.updateLecturer(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        lecturerService.deleteLecturer(id);

        return ResponseEntity.noContent().build();
    }
}
