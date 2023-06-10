package org.launchpad.launchpad_backend.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.launchpad.launchpad_backend.common.CommonUtils.getOrDefault;

@ControllerAdvice
public class ControllerValidationExceptionHandler extends ResponseEntityExceptionHandler {
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
}
