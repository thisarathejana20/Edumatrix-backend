package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.RoomAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationRepository extends JpaRepository<RoomAllocation, String> {
     boolean existsByStudentIdAndStatus(String studentId, String status);
}
