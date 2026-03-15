package edu.uni.edumatrix.repository;

import edu.uni.edumatrix.model.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceRepository
        extends JpaRepository<MaintenanceRequest, String> {
}
