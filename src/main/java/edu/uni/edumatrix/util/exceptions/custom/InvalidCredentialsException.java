package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        setType(ExType.INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(String message, Object... params) {
        super(message, params);
        setType(ExType.INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(String message) {
        super(message);
        setType(ExType.INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
