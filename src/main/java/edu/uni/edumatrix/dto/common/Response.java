package edu.uni.edumatrix.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class Response<T> {
    private T data;
    private Map<String, String> metaData;

    public Response(T data) {
        this.data = data;
    }

    public static <T>Response<T> of(T value) {
        return new Response<>(value);
    }
}
