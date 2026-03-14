package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class CourseAssignmentDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String lecturerId;

        @NotBlank
        private String courseId;

        private Integer academicYear;
        private Integer semester;

        private String role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String lecturerId;
        private String lecturerName;

        private String courseId;
        private String courseName;

        private Integer academicYear;
        private Integer semester;

        private LocalDate assignedDate;
        private String role;
    }
}
