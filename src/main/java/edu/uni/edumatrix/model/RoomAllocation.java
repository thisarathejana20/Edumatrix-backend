package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(
        name = "room_allocations",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id", "status"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    private LocalDate allocatedDate;

    private LocalDate vacatedDate;

    private String status; // ACTIVE / VACATED

    private Instant createdAt;
}
