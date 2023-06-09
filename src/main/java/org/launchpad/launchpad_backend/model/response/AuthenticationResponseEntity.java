package org.launchpad.launchpad_backend.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseEntity {
    private String requestedAccessToken;

}
