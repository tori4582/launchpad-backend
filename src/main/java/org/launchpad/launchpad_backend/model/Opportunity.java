package org.launchpad.launchpad_backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class Opportunity {

    @Id
    private String id;
    private String jobTitle;
    private String positionTitle;

    private Integer salaryRangeX;
    private Integer salaryRangeY;
    private String salaryCurrency;
    private String city;
    private String address3;
    private String address2;
    private String companyId;
    private JobTypeEnum jobType;
    private WorkingHoursEnum workingHours;
    private List<String> tags;
    private String description;
    private String postedUserId;
}
