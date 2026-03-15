package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.MaintenanceDTO;
import edu.uni.edumatrix.service.MaintenanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MaintenanceDTO.Response> create(
            @RequestBody @Valid MaintenanceDTO.CreateRequest req
    ) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestParam String status
    ) {
        service.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
