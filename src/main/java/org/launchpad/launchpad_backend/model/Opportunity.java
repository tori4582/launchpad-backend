package org.launchpad.launchpad_backend.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document
public class Opportunity implements Transformable {

    @Id
    private String id;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String positionTitle;

    @NotNull
    private Integer salaryRangeX;

    @NotNull
    private Integer salaryRangeY;

    @NotBlank
    private String salaryCurrency;

    @NotBlank
    private String city;

    @NotBlank
    private String address3;

    private String address2;

    @NotBlank
    private String companyId;

    @NotNull
    private JobTypeEnum jobType;

    @NotNull
    private WorkingHoursEnum workingHours;

    private List<String> tags;

    @NotBlank
    @Min(256)
    private String description;

    @NotBlank
    private String postedUserId;
}
