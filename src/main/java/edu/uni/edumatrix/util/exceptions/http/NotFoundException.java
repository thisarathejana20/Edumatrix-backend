package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException{
    public NotFoundException() {
        setType(ExType.NOT_FOUND);
    }

    public NotFoundException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.NOT_FOUND);
    }

    public NotFoundException(String message, Object... params) {
        super(message, params);
        setType(ExType.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(message);
        setType(ExType.NOT_FOUND);
    }

    public NotFoundException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.NOT_FOUND;
    }
}
