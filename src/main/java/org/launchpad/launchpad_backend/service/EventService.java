package org.launchpad.launchpad_backend.service;

import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.launchpad.launchpad_backend.model.Event;
import org.launchpad.launchpad_backend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<? extends Transformable> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<? extends Transformable> getOpeningEvents() {
        return eventRepository.findAllByRegistrationDeadlineAfter(LocalDateTime.now());
    }

    public List<? extends Transformable> getUpcomingEvents() {
        return eventRepository.findAllByStartingTimeAfter(LocalDateTime.now());
    }

    public Transformable getEventById(String id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public List<? extends Transformable> searchEventsByTitle(String title) {
        return eventRepository.findAllByEventTitleContainingIgnoreCase(title);
    }

    public Transformable createNewEvent(Event event) {
        return eventRepository.save(event);
    }

    public Transformable deleteEventById(String id) {
        var loadedEntity = this.getEventById(id);
        eventRepository.deleteById(id);
        return loadedEntity;
    }

    public Long deleteAllEvents() {
        var result = eventRepository.count();
        eventRepository.deleteAll();
        return result;
    }

}
