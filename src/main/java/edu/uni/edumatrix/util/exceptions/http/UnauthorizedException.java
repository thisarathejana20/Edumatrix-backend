package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException{
    public UnauthorizedException() {
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizedException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Object... params) {
        super(message, params);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(message);
        setType(ExType.UNAUTHORIZED);
    }

    public UnauthorizedException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
