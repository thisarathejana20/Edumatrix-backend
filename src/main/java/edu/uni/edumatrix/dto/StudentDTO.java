package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class StudentDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String departmentId;

        @NotBlank
        private String registrationNumber;

        @NotBlank
        private String fullName;

        @Email
        @NotBlank
        private String email;
        private String phone;

        private LocalDate dateOfBirth;
        private Integer admissionYear;

        private String status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String departmentId;
        private String departmentName;

        private String registrationNumber;

        private String fullName;

        private String email;
        private String phone;

        private LocalDate dateOfBirth;
        private Integer admissionYear;

        private String status;
    }
}
