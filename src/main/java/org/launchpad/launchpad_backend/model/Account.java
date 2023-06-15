package org.launchpad.launchpad_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document
public class Account implements Transformable {

    @Id
    private String id;

    @NotBlank
    private String username;

    @NotBlank
    private String displayName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 10, message = "Phone number must have ten numeric characters")
    @Pattern(regexp = "0[0-9]+", message = "Not a valid VN phone number")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8)
    @JsonIgnore
    private String hashedPassword;

    private String talentAcquisitionUserId;
    private String jobSeekerUserId;

    @NotNull
    private String bio;
    private LocalDate dateOfBirth;
    private Boolean isMale;
    private String avatarImageUrl;

    private String address;
    private String firstName;
    private String lastName;
    private String positionTitle;
    private String referenceUrl;
    private String linkedinUrl;
    private Integer experienceDurationInMonth;
    private List<String> skills;

    private AccountRoleEnum accountRole;

    private Map<AccountProviderEnum, String> associatedProviders;
}
