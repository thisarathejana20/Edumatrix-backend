package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BaseException{
    public AccessDeniedException() {
        setType(ExType.ACCESS_DENIED);
    }

    public AccessDeniedException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.ACCESS_DENIED);
    }

    public AccessDeniedException(String message, Object... params) {
        super(message, params);
        setType(ExType.ACCESS_DENIED);
    }

    public AccessDeniedException(String message) {
        super(message);
        setType(ExType.ACCESS_DENIED);
    }

    public AccessDeniedException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.FORBIDDEN;
    }
}
