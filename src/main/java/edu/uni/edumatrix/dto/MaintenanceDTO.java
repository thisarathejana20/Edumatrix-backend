package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class MaintenanceDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String roomId;

        @NotBlank
        private String issue;

        private String priority;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String hostelName;
        private String roomNumber;
        private String issue;
        private String priority;
        private String status;
    }
}
