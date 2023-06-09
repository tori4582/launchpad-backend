package org.launchpad.launchpad_backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class JobSeeker {

    @Id
    private String id;
    private String preferringJobType;
    private String preferringWorkingHours;
    private String postIds;
}
