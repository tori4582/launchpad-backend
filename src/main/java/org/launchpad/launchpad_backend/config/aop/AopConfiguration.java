package org.launchpad.launchpad_backend.config.aop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.launchpad.launchpad_backend.model.response.GeneralTransformableResponse;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Aspect
@Configuration
@RequiredArgsConstructor
public class AopConfiguration {

    private final ObjectMapper objectMapper;

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


}
