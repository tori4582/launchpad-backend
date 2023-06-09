package org.launchpad.launchpad_backend.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Event {
    
    @Id
    private String id;
    private String eventTitle;
    private String eventThumbnailUrl;
    private List<String> organiser;
    private List<String> sponsors;
    private String location;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private LocalDateTime startingTime;
    private LocalDateTime endTime;
    private Integer price;
    private LocalDateTime registrationDeadline;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @CreatedDate
    private LocalDateTime createdDateTime;
    private String htmlContent;
    private List<String> registratorIds;
    private List<String> interestedIds;

}
