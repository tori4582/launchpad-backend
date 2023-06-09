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
public class Post {

    @Id
    private String id;
    private String postTitle;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime lastUpdated;

    @CreatedBy
    private String createdBy;
    private String htmlContent;
    private String mainTopic;
    private List<String> tags;
}
