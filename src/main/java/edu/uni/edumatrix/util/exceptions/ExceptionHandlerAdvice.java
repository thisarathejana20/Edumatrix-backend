package edu.uni.edumatrix.util.exceptions;

import edu.uni.edumatrix.util.commons.RequestDataProvider;
import edu.uni.edumatrix.util.exceptions.custom.DatabaseException;
import edu.uni.edumatrix.util.exceptions.custom.ValidationException;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import edu.uni.edumatrix.util.exceptions.http.BaseException;
import edu.uni.edumatrix.util.exceptions.http.InternalErrorException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.sql.SQLException;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    private final RequestDataProvider requestDataProvider;

    public ExceptionHandlerAdvice(RequestDataProvider requestDataProvider) {
        this.requestDataProvider = requestDataProvider;
    }

    @ExceptionHandler(value = { BaseException.class})
    protected ResponseEntity<Object> handleCustomException(BaseException exception, HttpServletRequest request) {
        log.error("exception occurred [BaseException] type = {} error = {}",
                exception.getClass().getCanonicalName(), exception.getMessage());
        exception.printStackTrace();
        return exception.getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception exception) {
        log.error("exception occurred [Exception] error = {}", exception.getMessage());
        exception.printStackTrace();
        var internalExp = new InternalErrorException(exception.getMessage());
        return internalExp.getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> handleSQLException(SQLException exception) {
        log.error("SQL Exception", exception);
        return new DatabaseException("A database error occurred. Please try again later.")
                .getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    private static final Map<String, String> FRIENDLY_FIELD_MESSAGES = Map.of(
            "uk_user_email", "Email address is already in use",
            "uk_user_emp_id", "Employee ID is already registered",
            "uk_user_mobile", "Mobile number is already in use",
            "uk_asset_registerId" , "Asset ID is already in use",
            "uk_asset_serialNumber", "Serial Number is already in use",
            "uk_asset_macAddress", "MacAddress is already in use"
    );

    private String extractFriendlyMessageFromMySQL(String message) {
        int start = message.indexOf("for key '");
        if (start > 0) {
            start += 9;
            int end = message.indexOf("'", start);
            if (end > start) {
                String keyName = message.substring(start, end);

                if (keyName.contains(".")) {
                    keyName = keyName.substring(keyName.indexOf(".") + 1);
                }

                return FRIENDLY_FIELD_MESSAGES.getOrDefault(
                        keyName,
                        String.format("Unique constraint violation on field: %s", keyName)
                );
            }
        }
        return "Unique constraint violation";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.error("Data integrity violation", exception);

        String message = exception.getMessage() != null ? exception.getMessage().toLowerCase() : "";
        if (message.contains("duplicate")) {
            String friendlyMessage = extractFriendlyMessageFromMySQL(exception.getMessage());
            return new BadRequestException(friendlyMessage).getJsonAsResponse(requestDataProvider.getRequestHash());
        }
        return new DatabaseException("Invalid data").getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        StringBuilder message = new StringBuilder("Validation failed: ");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append("[")
                    .append(fieldError.getField())
                    .append(" - ")
                    .append(fieldError.getDefaultMessage())
                    .append("] ");
        }
        ValidationException exception = new ValidationException(message.toString().trim());
        return exception.getJsonAsResponse(requestDataProvider.getRequestHash());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        log.error("exception occurred [Malformed exception] error = {}", ex.getMessage());
        log.info("http response url = {}, response = {}",  request.getContextPath(), status.value());
        ex.printStackTrace();
        var exception = new BadRequestException("malformed request body. Please check for correct date/time formats and data types");
        return exception.getJsonAsResponse(requestDataProvider.getRequestHash());
    }
}
