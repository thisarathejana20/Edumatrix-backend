package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;

public class InternalErrorException extends BaseException{
    public InternalErrorException() {
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(String message, Object... params) {
        super(message, params);
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(String message) {
        super(message);
        setType(ExType.INTERNAL_ERROR);
    }

    public InternalErrorException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
