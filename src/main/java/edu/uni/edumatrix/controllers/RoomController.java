package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.RoomDTO;
import edu.uni.edumatrix.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomDTO.Response> create(
            @RequestBody @Valid RoomDTO.CreateRequest req
    ) {
        return ResponseEntity.ok(service.create(req));
    }
}
