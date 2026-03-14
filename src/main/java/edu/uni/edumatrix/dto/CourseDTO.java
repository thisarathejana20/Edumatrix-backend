package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CourseDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String departmentId;

        @NotBlank
        private String name;

        @NotBlank
        private String code;

        private Integer credits;

        private Integer semester;

        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private String id;

        private String departmentId;
        private String departmentName;

        private String name;
        private String code;

        private Integer credits;
        private Integer semester;

        private String description;
    }
}
