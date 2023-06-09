package org.launchpad.launchpad_backend.model.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class AuthenticationRequestEntity {

    @NonNull
    private String loginIdentity;

    @NonNull
    private String hashedPassword;
}
