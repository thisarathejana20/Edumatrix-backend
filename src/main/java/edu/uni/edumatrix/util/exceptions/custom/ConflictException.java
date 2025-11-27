package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException() {
        setType(ExType.CONFLICT);
    }

    public ConflictException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.CONFLICT);
    }

    public ConflictException(String message, Object... params) {
        super(message, params);
        setType(ExType.CONFLICT);
    }

    public ConflictException(String message) {
        super(message);
        setType(ExType.CONFLICT);
    }

    public ConflictException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.CONFLICT;
    }
}
