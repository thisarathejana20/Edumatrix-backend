package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_course_sem",
                        columnNames = {
                                "student_id",
                                "course_id",
                                "academic_year",
                                "semester"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    private Integer academicYear;
    private Integer semester;

    private Integer attemptNumber;

    private String status; // ENROLLED / DROPPED / COMPLETED

    private LocalDate enrolledDate;

}
