package edu.uni.edumatrix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String mobile;

    private String address;

    private String empId;

    private String designation;

    private String department;

    @Column(nullable = false)
    private String password;

    private String otp;

    @Column(nullable = false)
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_privileges", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "privilege")
    private List<String> privileges;

    @Column(name = "search_query", columnDefinition = "TEXT")
    private String query;
    private Instant createdAt;
    private Instant updatedAt;

    public void updateQuery() {
        StringBuilder sb = new StringBuilder();
        if (fullName != null) sb.append(fullName).append(" ");
        if (email != null) sb.append(email).append(" ");
        if (empId != null) sb.append(empId).append(" ");
        this.query = sb.toString().trim().toLowerCase();
    }

    @PrePersist
    public void prePersist() {
        updateQuery();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateQuery();
        this.updatedAt = Instant.now();
    }
}
