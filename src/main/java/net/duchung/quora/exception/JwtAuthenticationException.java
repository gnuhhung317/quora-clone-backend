package net.duchung.quora.entity;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
