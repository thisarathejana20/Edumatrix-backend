package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.HostelDTO;
import edu.uni.edumatrix.dto.RoomDTO;
import edu.uni.edumatrix.model.Hostel;
import edu.uni.edumatrix.model.Room;
import edu.uni.edumatrix.repository.HostelRepository;
import edu.uni.edumatrix.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RoomService {

    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;

    public RoomService(HostelRepository hostelRepository, RoomRepository roomRepository) {
        this.hostelRepository = hostelRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomDTO.Response create(RoomDTO.CreateRequest req) {

        Hostel hostel = hostelRepository.findById(req.getHostelId())
                .orElseThrow(() -> new RuntimeException("Hostel not found"));

        Room room = Room.builder()
                .id(UUID.randomUUID().toString())
                .hostel(hostel)
                .roomNumber(req.getRoomNumber())
                .capacity(req.getCapacity())
                .occupied(0)
                .type(req.getType())
                .createdAt(Instant.now())
                .build();

        roomRepository.save(room);

        return map(room);
    }

    private RoomDTO.Response map(Room room) {

        return new RoomDTO.Response(
                room.getId(),
                room.getHostel().getName(),
                room.getRoomNumber(),
                room.getCapacity(),
                room.getOccupied(),
                room.getType()
        );
    }
}
