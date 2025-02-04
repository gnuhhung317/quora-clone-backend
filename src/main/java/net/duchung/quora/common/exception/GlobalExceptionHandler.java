package net.duchung.quora.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import net.duchung.quora.data.response.BaseResponse;
import org.springframework.beans.PropertyAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DataNotFoundException.class, EntityNotFoundException.class, NoResourceFoundException.class})
    public BaseResponse<Object> handleDataNotFoundException(RuntimeException e) {
        return getErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, ServletException.class, IllegalArgumentException.class, PropertyAccessException.class, HttpMessageConversionException.class})
    public BaseResponse<Object> handleBadRequestException(RuntimeException e) {
        return getErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({JwtAuthenticationException.class, ExpiredJwtException.class, JwtException.class, UsernameNotFoundException.class})
    public BaseResponse<Object> handleAuthenticationException(RuntimeException e) {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return getErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BaseResponse<Object> handleAccessDeniedException(RuntimeException e) {
        return getErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> handleRuntimeException(RuntimeException e) {
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private BaseResponse<Object> getErrorResponse(HttpStatus status, String message) {
        return BaseResponse.error(status.value(), message);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public BaseResponse<Object> handleBadCredentials() {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @ExceptionHandler(DisabledException.class)
    public BaseResponse<Object> handleDisabledUser() {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, "User is disabled");
    }
}