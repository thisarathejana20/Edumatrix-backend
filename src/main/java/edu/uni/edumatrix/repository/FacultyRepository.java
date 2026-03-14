package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
    Optional<Faculty> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
