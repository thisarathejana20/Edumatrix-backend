package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Hostel;
import edu.uni.edumatrix.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostelRepository extends JpaRepository<Hostel, String> {
    boolean existsByNameIgnoreCase(String name);
}
