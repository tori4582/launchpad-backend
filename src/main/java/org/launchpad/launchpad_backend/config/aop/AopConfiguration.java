package org.launchpad.launchpad_backend.config.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.launchpad.launchpad_backend.common.ExceptionLogger;
import org.launchpad.launchpad_backend.common.SecurityHandler;
import org.launchpad.launchpad_backend.model.response.GeneralTransformableResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Aspect
@Configuration
@RequiredArgsConstructor
public class AopConfiguration {

    private final ObjectMapper objectMapper;
    private final SecurityHandler securityHandler;

    private final TypeReference<Map<String, Object>> responseTypeReference = new TypeReference<>() {};

    @Around("@annotation(org.launchpad.launchpad_backend.config.aop.TransformToResponseEntity)")
    public Transformable transformToResponseEntity(ProceedingJoinPoint joinPoint) throws Throwable {
        return new GeneralTransformableResponse(
                objectMapper.convertValue(joinPoint.proceed(), responseTypeReference)
        );
    }

    @Around("@annotation(org.launchpad.launchpad_backend.config.aop.MultipleTransformToResponseEntities)")
    public List<? extends Transformable> multipleTransformToResponseEntities(ProceedingJoinPoint joinPoint) throws Throwable {

        var result = (List<Transformable>) joinPoint.proceed();

        return result.stream()
                .map(transformableEntity -> objectMapper.convertValue(transformableEntity, responseTypeReference))
                .map(mapEntity -> (Transformable) new GeneralTransformableResponse(mapEntity))
                .toList();
    }

    @Before("@annotation(org.launchpad.launchpad_backend.config.aop.AllowedRoles)")
    public void roleGuarantee(JoinPoint joinPoint) throws Throwable {

        var typeClass = joinPoint.getSourceLocation().getWithinType();
        var controllerMethod = Arrays.stream(typeClass.getMethods())
                .filter(method -> method.getName().equals(joinPoint.getSignature().getName()))
                .findFirst()
                .orElseThrow();

        var indexOfAuthorizationToken = getIndexOfAuthorizationTokenFromMethodSignature(controllerMethod);
        if (indexOfAuthorizationToken == -1) {
            throw new NoSuchFieldException("No Authorization token found in method signature");
        }

        var authorizationToken = String.valueOf(joinPoint.getArgs()[indexOfAuthorizationToken]);

        var allowedRoles = List.of(controllerMethod.getAnnotation(AllowedRoles.class).value());

        securityHandler.roleGuaranteeForAop(authorizationToken, allowedRoles);
    }

    private int getIndexOfAuthorizationTokenFromMethodSignature(Method controllerMethod) {
        for (int i = 0; i < controllerMethod.getParameters().length; i++) {
            var requestHeaderAnnotation = controllerMethod.getParameters()[i].getAnnotation(RequestHeader.class);
            if (requestHeaderAnnotation.value().equals("Authorization")) {
                return i;
            }
        }
        return -1;
    }

    @Before("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    public void logExceptionHandlerContent(JoinPoint joinPoint) {
        ExceptionLogger.logInvalidAction((Exception) joinPoint.getArgs()[0]);
    }

}
