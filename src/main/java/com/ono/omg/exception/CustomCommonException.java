package com.ono.omg.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomCommonException extends RuntimeException {
    private final int status;
    private final HttpStatus httpStatus;

    public CustomCommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
        this.httpStatus = errorCode.getHttpStatus();
    }
}