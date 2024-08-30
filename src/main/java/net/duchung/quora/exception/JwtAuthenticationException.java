package net.duchung.quora.exception;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
