package com.vention.examinai.notification_service.exception;

import com.vention.examinai.notification_service.enums.ResponseStatus;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ResponseStatus responseStatus;

    public ApiException(ResponseStatus responseStatus) {
        super(responseStatus.name());
        this.responseStatus = responseStatus;
    }
}
