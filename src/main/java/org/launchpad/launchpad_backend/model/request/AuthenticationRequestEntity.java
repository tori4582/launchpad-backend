package org.launchpad.launchpad_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@Builder
public class AuthenticationRequestEntity {

    @NonNull
    @NotBlank
    private String loginIdentity;

    @NonNull
    @NotBlank
    private String hashedPassword;
}
