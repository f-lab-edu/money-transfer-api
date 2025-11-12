package com.money.transfer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorResponse errorResponse;

    public AuthException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
        this.errorResponse = new ErrorResponse(message);
    }
}
