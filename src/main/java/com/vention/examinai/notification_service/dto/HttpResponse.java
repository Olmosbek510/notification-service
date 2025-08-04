package com.vention.examinai.notification_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vention.examinai.notification_service.enums.ResponseStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@JsonInclude(NON_NULL)
public class HttpResponse {
    private Integer statusCode;
    private String description;
    private Object data;
    private ResponseStatus responseStatus;
}
