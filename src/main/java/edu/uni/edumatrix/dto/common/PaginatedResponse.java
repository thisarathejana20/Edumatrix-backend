package edu.uni.edumatrix.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaginatedResponse<T> {
    private T data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public PaginatedResponse(T data) {
        this.data = data;
    }

    public static <T>PaginatedResponse<T> of(T value) {
        return new PaginatedResponse<>(value);
    }
}

