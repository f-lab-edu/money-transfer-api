package com.money.transfer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorResponse errorResponse;

    public UserException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
        this.errorResponse = new ErrorResponse(message);
    }
}
