package org.launchpad.launchpad_backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
public class TalentAcquisition {

    @Id
    private String id;
    private List<String> postedOpportunityIds;
    private List<String> postedEventIds;
    private List<String> postIds;
}
