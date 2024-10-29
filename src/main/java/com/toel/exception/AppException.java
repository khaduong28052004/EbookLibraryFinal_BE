package com.toel.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException {
    ErrorCode errorCode;
    Object[] params;

    public AppException(ErrorCode errorCode, Object... params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    @Override
    public String getMessage() {
        return errorCode.formatMessage(params);
    }
}
