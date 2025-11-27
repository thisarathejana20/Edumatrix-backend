package edu.uni.edumatrix.util.exceptions;

public enum ExType implements ExceptionType {
    BAD_REQUEST,
    UNAUTHORIZED,
    CONFLICT,
    INTERNAL_ERROR,
    NOT_FOUND,
    ACCESS_DENIED,
    TOO_MANY_REQUESTS,
    VALIDATION_ERROR,
    DATABASE_ERROR,
    INVALID_CREDENTIALS,
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    USER_INACTIVE,
    VERIFICATION_TOKEN_INVALID,
    VERIFICATION_TOKEN_EXPIRED,
    PASSWORD_RESET_TOKEN_INVALID,
    PASSWORD_RESET_TOKEN_EXPIRED,
    REFRESH_TOKEN_INVALID,
    REFRESH_TOKEN_EXPIRED;

    @Override
    public String getType() {
        return this.toString();
    }
}
