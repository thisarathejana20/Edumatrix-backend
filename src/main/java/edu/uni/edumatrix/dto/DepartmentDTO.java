package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class DepartmentDTO {

    @Getter
    @Setter
    public static class CreateRequest {
        @NotNull
        private Long facultyId;

        @NotBlank
        @Size(max=150)
        private String name;

        @Size(max=50)
        private String code;

        private String description;
    }

    @Getter @Setter @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long facultyId;
        private String facultyName;
        private String name;
        private String code;
        private String description;
    }
}
