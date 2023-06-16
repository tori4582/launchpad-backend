package org.launchpad.launchpad_backend.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.aspectj.lang.annotation.Before;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Event implements Transformable {
    
    @Id
    private String id;

    @NotBlank
    private String eventTitle;

    private String eventThumbnailUrl;

    @NotEmpty
    private List<String> organiser;

    private List<String> sponsors;

    @NotBlank
    private String location;

    @NotBlank
    private String address1;

    @NotBlank
    private String address2;

    @NotBlank
    private String address3;

    @NotBlank
    private String address4;

    @Future
    private LocalDateTime startingTime;

    @Future
    private LocalDateTime endTime;

    @PositiveOrZero
    private Integer price;

    @FutureOrPresent
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
