package com.example.fruitmachine.web.error;

import com.example.fruitmachine.domain.InvalidMachineConfigException;
import com.example.fruitmachine.service.MachineNotConfiguredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return status(HttpStatus.BAD_REQUEST, message.isBlank() ? "Invalid request" : message);
    }

    @ExceptionHandler(InvalidMachineConfigException.class)
    public ResponseEntity<ApiError> onInvalidConfig(InvalidMachineConfigException ex) {
        return status(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MachineNotConfiguredException.class)
    public ResponseEntity<ApiError> onNotConfigured(MachineNotConfiguredException ex) {
        return status(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<ApiError> status(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiError.of(status.value(), message));
    }
}
