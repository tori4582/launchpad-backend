package org.launchpad.launchpad_backend.model.response;


import org.launchpad.launchpad_backend.config.aop.Transformable;

import java.util.Map;

public record GeneralTransformableResponse(Map<String, Object> responseData) implements Transformable {

}
