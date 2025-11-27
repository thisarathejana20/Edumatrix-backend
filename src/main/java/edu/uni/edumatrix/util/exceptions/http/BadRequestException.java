package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class BadRequestException extends BaseException{
    public BadRequestException() {
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(String message, Object... params) {
        super(message, params);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(message);
        setType(ExType.BAD_REQUEST);
    }

    public BadRequestException(Set<? extends ConstraintViolation<?>> violations) {
        super(buildMessage(violations));
        setType(ExType.BAD_REQUEST);
    }

    private static String buildMessage(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining(", "));
    }

    public BadRequestException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
