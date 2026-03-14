package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class GradeDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String enrollmentId;

        private Double marks;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String studentName;
        private String courseName;

        private Double marks;
        private String grade;
        private Double gradePoint;
        private String resultStatus;

        private LocalDate publishedDate;
    }
}
