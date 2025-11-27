package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class UtilityTokenInvalidException extends BaseException {
    public UtilityTokenInvalidException() {
        setType(ExType.TOKEN_INVALID);
    }

    public UtilityTokenInvalidException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.TOKEN_INVALID);
    }

    public UtilityTokenInvalidException(String message, Object... params) {
        super(message, params);
        setType(ExType.TOKEN_INVALID);
    }

    public UtilityTokenInvalidException(String message) {
        super(message);
        setType(ExType.TOKEN_INVALID);
    }

    public UtilityTokenInvalidException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
