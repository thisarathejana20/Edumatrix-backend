package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import org.springframework.http.HttpStatus;

public class ValidationException extends BadRequestException {

    public ValidationException() {
        setType(ExType.VALIDATION_ERROR);
    }

    public ValidationException(String message, Object... params) {
        super(message, params);
        setType(ExType.VALIDATION_ERROR);
    }

    public ValidationException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.VALIDATION_ERROR);
    }

    public ValidationException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
