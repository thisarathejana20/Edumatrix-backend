package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.AllocationDTO;
import edu.uni.edumatrix.model.Room;
import edu.uni.edumatrix.model.RoomAllocation;
import edu.uni.edumatrix.model.Student;
import edu.uni.edumatrix.repository.AllocationRepository;
import edu.uni.edumatrix.repository.RoomRepository;
import edu.uni.edumatrix.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class AllocationService {

    private final StudentRepository studentRepository;
    private final RoomRepository roomRepository;
    private final AllocationRepository allocationRepository;

    public AllocationService(StudentRepository studentRepository,
                             RoomRepository roomRepository,
                             AllocationRepository allocationRepository) {
        this.studentRepository = studentRepository;
        this.roomRepository = roomRepository;
        this.allocationRepository = allocationRepository;
    }

    @Transactional
    public AllocationDTO.Response allocate(
            AllocationDTO.AllocateRequest req
    ) {

        Student student = studentRepository.findById(req.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Room room = roomRepository.findById(req.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 1️⃣ Check already allocated
        boolean existsActive =
                allocationRepository.existsByStudentIdAndStatus(
                        student.getId(), "ACTIVE"
                );

        if (existsActive) {
            throw new RuntimeException(
                    "Student already has active room"
            );
        }

        // 2️⃣ Check capacity
        if (room.getOccupied() >= room.getCapacity()) {
            throw new RuntimeException("Room is full");
        }

        // 3️⃣ Create allocation
        RoomAllocation allocation = RoomAllocation.builder()
                .id(UUID.randomUUID().toString())
                .student(student)
                .room(room)
                .allocatedDate(LocalDate.now())
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        allocationRepository.save(allocation);

        // 4️⃣ Increment occupied
        room.setOccupied(room.getOccupied() + 1);
        roomRepository.save(room);

        return map(allocation);
    }

    @Transactional
    public void vacate(String allocationId) {

        RoomAllocation allocation =
                allocationRepository.findById(allocationId)
                        .orElseThrow(() ->
                                new RuntimeException("Not found"));

        if (!"ACTIVE".equals(allocation.getStatus())) {
            throw new RuntimeException("Already vacated");
        }

        allocation.setStatus("VACATED");
        allocation.setVacatedDate(LocalDate.now());

        Room room = allocation.getRoom();
        room.setOccupied(room.getOccupied() - 1);

    }

    private AllocationDTO.Response map(RoomAllocation allocation) {

        return new AllocationDTO.Response(
                allocation.getId(),
                allocation.getStudent().getUser().getFullName(),
                allocation.getRoom().getRoomNumber(),
                allocation.getRoom().getHostel().getName(),
                allocation.getStatus()
        );
    }
}
