package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_faculty_name", columnNames = {"name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable=false, length=150)
    private String name;

    @Column(length=50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;
}
