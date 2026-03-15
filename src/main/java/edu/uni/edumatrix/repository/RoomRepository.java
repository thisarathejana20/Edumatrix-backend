package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
