package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "department",
        uniqueConstraints = {
                // department name must be unique inside a faculty
                @UniqueConstraint(name = "uk_dept_faculty_name", columnNames = {"faculty_id", "name"})
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
