package edu.uni.edumatrix.util.exceptions.http;

import edu.uni.edumatrix.util.exceptions.ExceptionType;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

public class BaseException extends RuntimeException{
    private String message;
    @Setter
    private ExceptionType type;
    private Exception rootException;

    public BaseException() {
    }

    public BaseException(Exception rootException, String message, Object... params) {
        super(message);
        this.message = formattedMessage(message, params);
        this.rootException = rootException;
    }

    public BaseException(String message, Object... params) {
        super(message);
        this.message = formattedMessage(message, params);
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(ExceptionType type, String message) {
        super(message);
        this.type = type;
        this.message = message;
    }

    public BaseException(ExceptionType type, String message, Object... params) {
        super(message);
        this.type = type;
        this.message = formattedMessage(message, params);
    }

    public BaseException(Exception rootException, ExceptionType type, String message, Object... params) {
        super(message);
        this.type = type;
        this.message = formattedMessage(message, params);
        this.rootException = rootException;
    }

    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getJsonAsString(String hash) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode object = mapper.createObjectNode();
        object.put("message", this.message);
        object.put("type", this.type.getType());
        object.put("hash", hash);
        return object.toString();
    }

    public ResponseEntity<Object> getJsonAsResponse() {
        return ResponseEntity.status(this.getCode().value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.getJsonAsString(null));
    }

    public ResponseEntity<Object> getJsonAsResponse(String requestHash) {
        return ResponseEntity.status(this.getCode().value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.getJsonAsString(requestHash));
    }

    private String formattedMessage(String msg, Object[] params) {
        for (Object param : params) {
            msg = msg.replaceFirst("\\{\\}", param != null ? param.toString() : "null");
        }
        return msg;
    }

    public String getMessage() {
        if(rootException == null) {
            return this.message;
        }
        return this.message + " : root exception = " + rootException.getMessage();
    }
}
