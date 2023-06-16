package org.launchpad.launchpad_backend.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.launchpad.launchpad_backend.common.ExceptionLogger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.launchpad.launchpad_backend.common.CommonUtils.getOrDefault;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        return new ResponseEntity<>(
                ex.getBindingResult().getFieldErrors()
                    .stream()
                    .map(error -> Map.of(
                                    "invalidField", error.getField(),
                                    "rejectedValue", getOrDefault(error.getRejectedValue(), "null"),
                                    "rejectedReason", getOrDefault(error.getDefaultMessage(), "")
                            )
                    ).toList(),
                headers,
                status
        );
    }

    @ExceptionHandler({NoSuchElementException.class, NullPointerException.class})
    public ResponseEntity<?> handleNotFound(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoSuchFieldException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleBadRequest(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalAccessException.class, ExpiredJwtException.class})
    public ResponseEntity<?> handleUnauthorised(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UnsupportedOperationException.class, Exception.class})
    public ResponseEntity<?> handleInternalServerError(Exception ex) {
        return new ResponseEntity<>(
                ExceptionLogger.ResponseException.fromExceptionObject(ex),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
