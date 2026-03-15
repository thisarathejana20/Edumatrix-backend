package edu.uni.edumatrix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RoomDTO {

    @Getter
    @Setter
    public static class CreateRequest {

        @NotBlank
        private String hostelId;

        @NotBlank
        private String roomNumber;

        @NotNull
        private Integer capacity;

        private String type;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String hostelName;
        private String roomNumber;
        private Integer capacity;
        private Integer occupied;
        private String type;
    }
}
