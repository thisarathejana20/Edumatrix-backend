package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.MaintenanceDTO;
import edu.uni.edumatrix.dto.RoomDTO;
import edu.uni.edumatrix.model.MaintenanceRequest;
import edu.uni.edumatrix.model.Room;
import edu.uni.edumatrix.repository.MaintenanceRepository;
import edu.uni.edumatrix.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MaintenanceService {

    private final MaintenanceRepository repository;
    private final RoomRepository roomRepository;

    public MaintenanceService(MaintenanceRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public MaintenanceDTO.Response create(
            MaintenanceDTO.CreateRequest req
    ) {

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        MaintenanceRequest m = MaintenanceRequest.builder()
                .id(UUID.randomUUID().toString())
                .room(room)
                .issue(req.getIssue())
                .priority(req.getPriority())
                .status("OPEN")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        repository.save(m);

        return map(m);
    }

    @Transactional
    public void updateStatus(String id, String status) {

        MaintenanceRequest m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        m.setStatus(status);
        m.setUpdatedAt(Instant.now());
    }

    private MaintenanceDTO.Response map(MaintenanceRequest m) {

        return new MaintenanceDTO.Response(
                m.getId(),
                m.getRoom().getHostel().getName(),
                m.getRoom().getRoomNumber(),
                m.getIssue(),
                m.getPriority(),
                m.getStatus()
        );
    }
}

