package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.AllocationDTO;
import edu.uni.edumatrix.service.AllocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room-allocations")
public class RoomAllocationController {

    private final AllocationService service;

    public RoomAllocationController(AllocationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AllocationDTO.Response> allocate(
            @RequestBody @Valid AllocationDTO.AllocateRequest req
    ) {
        return ResponseEntity.ok(service.allocate(req));
    }

    @PostMapping("/vacate/{allocationId}/vacate")
    public ResponseEntity<Void> vacate(@PathVariable String allocationId) {
        service.vacate(allocationId);
        return ResponseEntity.ok().build();
    }
}
