package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "maintenance_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false, length = 500)
    private String issue;

    private String priority; // LOW / MEDIUM / HIGH

    private String status; // OPEN / IN_PROGRESS / RESOLVED

    private Instant createdAt;

    private Instant updatedAt;
}
