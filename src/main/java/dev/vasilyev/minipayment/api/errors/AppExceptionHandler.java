package dev.vasilyev.minipayment.api.errors;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        log.error("MethodArgumentNotValidException ", exception);
        String collectedErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        int statusVal = exception.getStatusCode().value();
        ApiError errorBody = new ApiError(
                LocalDateTime.now(),
                statusVal,
                "validation failed",
                "Validation failed: " + collectedErrors,
                request.getRequestURI()
        );
        return ResponseEntity
                .status(statusVal)
                .body(errorBody);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request) {
        log.error("ResponseStatusException ", exception);

        int statusVal = exception.getStatusCode().value();
        ApiError errorBody = new ApiError(
                LocalDateTime.now(),
                statusVal,
                exception.getReason(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(statusVal)
                .body(errorBody);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception exception,
            HttpServletRequest request) {
        log.error("Unexpected error", exception);

        ApiError errorBody = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody);
    }
}
