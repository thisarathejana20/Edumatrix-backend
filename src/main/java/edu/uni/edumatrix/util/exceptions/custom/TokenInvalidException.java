package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class TokenInvalidException extends BaseException {
    public TokenInvalidException() {
        setType(ExType.TOKEN_INVALID);
    }

    public TokenInvalidException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.TOKEN_INVALID);
    }

    public TokenInvalidException(String message, Object... params) {
        super(message, params);
        setType(ExType.TOKEN_INVALID);
    }

    public TokenInvalidException(String message) {
        super(message);
        setType(ExType.TOKEN_INVALID);
    }

    public TokenInvalidException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
