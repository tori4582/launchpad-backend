package org.launchpad.launchpad_backend.repository;

import org.launchpad.launchpad_backend.model.Opportunity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

public interface OpportunityRepository extends MongoRepository<Opportunity, String> {

    List<Opportunity> findAllByJobTitleContainingIgnoreCase(String titleQuery);

    List<Opportunity> findAllByPositionTitleContainingIgnoreCase(String positionQuery);

}
