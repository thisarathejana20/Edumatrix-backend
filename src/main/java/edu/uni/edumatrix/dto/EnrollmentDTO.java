package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class EnrollmentDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String studentId;

        @NotBlank
        private String courseId;

        private Integer academicYear;
        private Integer semester;
        private Integer attemptNumber;

        private String status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String studentId;
        private String studentName;

        private String courseId;
        private String courseName;

        private Integer academicYear;
        private Integer semester;
        private Integer attemptNumber;

        private String status;
        private LocalDate enrolledDate;
    }
}
