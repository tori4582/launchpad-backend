package org.launchpad.launchpad_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.common.SecurityHandler;
import org.launchpad.launchpad_backend.config.aop.AllowedRoles;
import org.launchpad.launchpad_backend.model.AccountRoleEnum;
import org.launchpad.launchpad_backend.model.Opportunity;
import org.launchpad.launchpad_backend.service.OpportunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.launchpad.launchpad_backend.common.ControllerUtils.controllerWrapper;
import static org.launchpad.launchpad_backend.model.AccountRoleEnum.TALENT_ACQUISITION;

@RestController
@RequestMapping("/opportunities")
@RequiredArgsConstructor
@Validated
public class OpportunityController {

    private final OpportunityService opportunityService;

    // READ operations
    @GetMapping
    public ResponseEntity<?> getAllOpportunities() {
        return controllerWrapper(opportunityService::getAllOpportunities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOpportunityById(@PathVariable String id) {
        return controllerWrapper(() -> opportunityService.getOpportunityById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchOpportunitiesByCriteria(@RequestParam String titleQuery,
                                                           @RequestParam List<String> tagsQuery,
                                                           @RequestParam String positionQuery) {
        return controllerWrapper(() -> opportunityService.searchOpportunitiesByCriteria(titleQuery, tagsQuery, positionQuery));
    }

    // CREATE operations

    @AllowedRoles({TALENT_ACQUISITION})
    public ResponseEntity<?> createNewOpportunity(@RequestHeader String authorizationToken,
                                                  @RequestBody @Valid Opportunity opportunity) throws Throwable {
        return controllerWrapper(() -> opportunityService.createNewOpportunity(opportunity));
    }

    // MODIFY operations
    @PostMapping("/{id}")
    @AllowedRoles({TALENT_ACQUISITION})
    public ResponseEntity<?> updateExistingOpportunity(@RequestHeader String authorizationToken,
                                                       @PathVariable String id,
                                                       @RequestBody @Valid Opportunity opportunity) throws Throwable {
        return controllerWrapper(() -> opportunityService.updateExistingOpportunity(id, opportunity));
    }

    // DELETE operations
    @DeleteMapping("/{id}")
    @AllowedRoles({TALENT_ACQUISITION})
    public ResponseEntity<?> deleteOpportunityById(@RequestHeader String authorizationToken,
                                                   @PathVariable String id) throws Throwable {
        return controllerWrapper(() -> opportunityService.deleteOpportunityById(id));
    }

    @DeleteMapping("/dev/all")
    public ResponseEntity<?> deleteAllOpportunities() {
        return controllerWrapper(opportunityService::deleteAllOpportunities);
    }

}
