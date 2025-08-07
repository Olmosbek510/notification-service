package com.vention.examinai.notification_service.exception.handler;

import com.vention.examinai.notification_service.config.ExceptionReporter;
import com.vention.examinai.notification_service.dto.HttpResponse;
import com.vention.examinai.notification_service.dto.response.ErrorResponse;
import com.vention.examinai.notification_service.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionReporter exceptionReporter;


    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> handleApiException(ApiException e) {
        log.error(e.getResponseStatus().name().concat(": ").concat(e.getResponseStatus().getDescription()));

        exceptionReporter.report(e);

        return ResponseEntity
            .status(e.getResponseStatus().getHttpStatus())
            .body(
                HttpResponse.builder()
                    .statusCode(e.getResponseStatus().getStatusCode())
                    .description(e.getResponseStatus().getDescription())
                    .responseStatus(e.getResponseStatus())
                    .build()
            );
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(HttpServerErrorException.InternalServerError ex, HttpServletRequest request) {
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        ErrorResponse errorResponse = new ErrorResponse(
                statusCode,
                ex.getMessage()
        );

        exceptionReporter.report(ex, request, statusCode);


        return ResponseEntity.status(statusCode).body(errorResponse);
    }
}
