package net.duchung.quora.common.exception;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
