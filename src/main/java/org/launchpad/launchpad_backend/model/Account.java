package org.launchpad.launchpad_backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Account {

    @Id
    private String id;
    private String username;

    private String displayName;
    private String email;
    private String hashedPassword;
    private String talentAcquisitionUserId;
    private String jobSeekerUserId;

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
    private String phoneNumber;

    private AccountRoleEnum accountRole;
    private Map<AccountProviderEnum, String> associatedProviders;
}
