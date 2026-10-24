
package in.startupready.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleStatusException(
            ResponseStatusException exception) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", exception.getStatusCode().value());
        body.put("error", exception.getReason() == null
                ? "Request failed"
                : exception.getReason());

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException exception) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
    }


@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, Object>> handleUnexpectedException(
        Exception exception) {

    exception.printStackTrace();

    return buildError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. Please try again.");
}


    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status,
            String message) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", message);

        return ResponseEntity.status(status).body(body);
    }
}
