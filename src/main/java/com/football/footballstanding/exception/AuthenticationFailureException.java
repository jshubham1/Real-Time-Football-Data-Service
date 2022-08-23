package com.football.footballstanding.exception;

public class AuthenticationFailureException extends RuntimeException {
    public AuthenticationFailureException() {
        super("Authentication failed! Invalid API key, please check your API Key");
    }
}
