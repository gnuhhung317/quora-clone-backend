package net.duchung.quora.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.SerializationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import net.duchung.quora.data.response.ApiResponse;
import org.springframework.beans.PropertyAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataNotFoundException.class,EntityNotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<String> handleDataNotFoundException(RuntimeException e ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler({HttpMessageNotReadableException.class,ServletException.class,IllegalArgumentException.class,PropertyAccessException.class,HttpMessageConversionException.class})
    public ResponseEntity<String> handleBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler({JwtAuthenticationException.class,JwtException.class,UsernameNotFoundException.class})
    public ResponseEntity<String> handleAuthenticationException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    private ApiResponse<?> getErrorResponse(HttpStatus status, String message) {
        return ApiResponse.error(status.value(), message);
    }
}
