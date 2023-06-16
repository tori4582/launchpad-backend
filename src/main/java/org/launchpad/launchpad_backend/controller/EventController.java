package org.launchpad.launchpad_backend.controller;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.config.aop.AllowedRoles;
import org.launchpad.launchpad_backend.model.AccountRoleEnum;
import org.launchpad.launchpad_backend.model.Event;
import org.launchpad.launchpad_backend.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.launchpad.launchpad_backend.common.ControllerUtils.controllerWrapper;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<?> getAllEvents(@RequestParam String status) {

        if (Objects.requireNonNull(status, "").equalsIgnoreCase("opening")) {
            return controllerWrapper(eventService::getOpeningEvents);
        }

        if (Objects.requireNonNull(status, "").equalsIgnoreCase("upcoming")) {
            return controllerWrapper(eventService::getUpcomingEvents);
        }

        return controllerWrapper(eventService::getAllEvents);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEventsByTitle(@RequestParam String titleQuery) {
        return controllerWrapper(() -> eventService.searchEventsByTitle(titleQuery));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        return controllerWrapper(() -> eventService.getEventById(id));
    }

    @PostMapping
    @AllowedRoles({AccountRoleEnum.TALENT_ACQUISITION})
    public ResponseEntity<?> createNewEvent(@RequestBody @Valid Event event) throws Throwable {
        return controllerWrapper(() -> eventService.createNewEvent(event));
    }

    @DeleteMapping("/{id}")
    @AllowedRoles({AccountRoleEnum.TALENT_ACQUISITION})
    public ResponseEntity<?> deleteEventById(@PathVariable String id) throws Throwable {
        return controllerWrapper(() -> eventService.deleteEventById(id));
    }

    @DeleteMapping("/dev/all")
    public ResponseEntity<?> deleteAllEvents() {
        return controllerWrapper(eventService::deleteAllEvents);
    }

}
