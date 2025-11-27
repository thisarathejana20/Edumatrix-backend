package edu.uni.edumatrix.util.exceptions.custom;

import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.ExceptionType;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import org.springframework.http.HttpStatus;

public class DatabaseException extends BaseException {
    public DatabaseException() {
        setType(ExType.NOT_FOUND);
    }

    public DatabaseException(Exception rootException, String message, Object... params) {
        super(rootException, message, params);
        setType(ExType.DATABASE_ERROR);
    }

    public DatabaseException(String message, Object... params) {
        super(message, params);
        setType(ExType.DATABASE_ERROR);
    }

    public DatabaseException(String message) {
        super(message);
        setType(ExType.DATABASE_ERROR);
    }

    public DatabaseException(ExceptionType type, String message, Object... params) {
        super(type, message, params);
    }

    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
