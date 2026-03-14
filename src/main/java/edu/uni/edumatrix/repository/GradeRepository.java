package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeRepository
        extends JpaRepository<Grade, String> {

    Optional<Grade> findByEnrollmentId(String enrollmentId);

}
