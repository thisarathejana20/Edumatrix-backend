package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "department",
        uniqueConstraints = {
                // department name must be unique inside a faculty
                @UniqueConstraint(name = "uk_dept_faculty_name", columnNames = {"faculty_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="faculty_id", nullable=false)
    private Faculty faculty;

    @Column(nullable=false, length=150)
    private String name;

    @Column(length=50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;
}
