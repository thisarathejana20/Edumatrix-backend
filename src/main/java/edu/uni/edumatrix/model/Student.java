package edu.uni.edumatrix.model;

import edu.uni.edumatrix.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_reg_no",
                        columnNames = "registration_number"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    private LocalDate dateOfBirth;

    private Integer admissionYear;

    private String status; // ACTIVE / SUSPENDED / GRADUATED

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            unique = true,
            nullable = false
    )
    private User user;

}
