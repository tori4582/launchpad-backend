package org.launchpad.launchpad_backend.repository;

import org.launchpad.launchpad_backend.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findAllByStartingTimeAfter(LocalDateTime timestamp);
    List<Event> findAllByRegistrationDeadlineAfter(LocalDateTime timestamp);

    List<Event> findAllByEventTitleContainingIgnoreCase(String titleQuery);
}
