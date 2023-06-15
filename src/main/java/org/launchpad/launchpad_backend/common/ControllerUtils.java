package org.launchpad.launchpad_backend.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.launchpad.launchpad_backend.model.response.GeneralTransformableResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static org.launchpad.launchpad_backend.common.ExceptionLogger.logInvalidAction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ControllerUtils {

    private static final Map<Class<? extends Exception>, ? extends ResponseEntity.HeadersBuilder<?>> exceptionalResponseMappings = Map.of(
            NoSuchElementException.class, ResponseEntity.notFound(),
            NullPointerException.class, ResponseEntity.notFound(),
            IllegalArgumentException.class, ResponseEntity.badRequest(),
            NoSuchFieldException.class, ResponseEntity.badRequest(),
            UnsupportedOperationException.class, ResponseEntity.internalServerError()
    );

    public static ResponseEntity<?> controllerWrapper(Supplier<?> controllerExecution) {
        try {
            var controllerBehaviorResult = controllerExecution.get();

            if (controllerBehaviorResult instanceof GeneralTransformableResponse generalTransformableResponse) {
                return ResponseEntity.ok(generalTransformableResponse.responseData());
            }

            if (controllerBehaviorResult instanceof List) {
                return ResponseEntity.ok(
                        ((List<?>) controllerBehaviorResult).stream()
                                .map(e -> ((GeneralTransformableResponse) e).responseData())
                                .toList()
                );
            }

            return ResponseEntity.ok(controllerBehaviorResult);

        } catch (Exception e) {
            logInvalidAction(e);
            return switchExceptionsResponse(e);
        }
    }

    public static ResponseEntity<?> controllerWrapper(Runnable controllerExecution) {
        try {
            controllerExecution.run();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logInvalidAction(e);
            return switchExceptionsResponse(e);
        }
    }

    private static ResponseEntity<?> switchExceptionsResponse(Exception e) {

        if (exceptionalResponseMappings.containsKey(e.getClass())) {
            return ResponseEntity.internalServerError().body(ExceptionLogger.ResponseException.fromExceptionObject(e));
        }

        return ((ResponseEntity.BodyBuilder) exceptionalResponseMappings.get(e.getClass())).body(e.getMessage());
    }
}
