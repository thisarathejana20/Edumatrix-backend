package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AllocationDTO {

    @Getter
    @Setter
    public static class AllocateRequest {

        @NotBlank
        private String studentId;

        @NotBlank
        private String roomId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String studentName;
        private String roomNumber;
        private String hostelName;
        private String status;
    }
}
