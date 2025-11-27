package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        setType(ExType.TOKEN_EXPIRED);
    }

    public TokenExpiredException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.TOKEN_EXPIRED);
    }

    public TokenExpiredException(String message, Object... params) {
        super(message, params);
        setType(ExType.TOKEN_EXPIRED);
    }

    public TokenExpiredException(String message) {
        super(message);
        setType(ExType.TOKEN_EXPIRED);
    }

    public TokenExpiredException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
