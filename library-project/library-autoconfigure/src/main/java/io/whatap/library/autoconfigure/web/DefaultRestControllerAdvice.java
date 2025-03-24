package io.whatap.library.autoconfigure.web;

import io.whatap.library.shared.message.ErrorResponse;
import io.whatap.library.shared.web.client.ExtendedHttpClientErrorException;
import io.whatap.library.shared.web.client.ExtendedHttpServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class DefaultRestControllerAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(genHttpStatus(e))
                .body(new ErrorResponse("9999", e.getMessage()));
    }

    @ExceptionHandler({ExtendedHttpClientErrorException.class})
    public ResponseEntity<ErrorResponse> handleExtendedHttpClientErrorException(ExtendedHttpClientErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler({ExtendedHttpServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleExtendedHttpServerErrorException(ExtendedHttpServerErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }


    private static HttpStatus genHttpStatus(Exception e) {
        if (e instanceof EntityNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
