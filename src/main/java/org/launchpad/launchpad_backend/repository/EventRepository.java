package org.launchpad.launchpad_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "event", path = "events")
public interface EventRepository extends MongoRepository<EventRepository, String> {

    List<EventRepository> findAll();
    Optional<EventRepository> findById(@Param("id") String eventId);


}
