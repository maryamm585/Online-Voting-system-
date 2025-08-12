package com.votingSystem.voting.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private ApiError build(HttpStatus status, String message, String path) {
        return new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message, path, null);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ApiError> handleInvalidLogin(InvalidLoginException ex, HttpServletRequest req) {
        ApiError err = build(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    @ExceptionHandler(DoubleVotingException.class)
    public ResponseEntity<ApiError> handleDoubleVoting(DoubleVotingException ex, HttpServletRequest req) {
        ApiError err = build(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity<ApiError> handleVoteNotFound(VoteNotFoundException ex, HttpServletRequest req) {
        ApiError err = build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(VotingClosedException.class)
    public ResponseEntity<ApiError> handleVotingClosed(VotingClosedException ex, HttpServletRequest req) {
        ApiError err = build(HttpStatus.FORBIDDEN, ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    // for @RequestBody fields
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a,b) -> a)); // merge duplicates
        ApiError apiError = new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "Validation failed", req.getRequestURI(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // for @PathVariable / @RequestParam violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (a,b) -> a));
        ApiError apiError = new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "Constraint violation", req.getRequestURI(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        ApiError apiError = build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}

