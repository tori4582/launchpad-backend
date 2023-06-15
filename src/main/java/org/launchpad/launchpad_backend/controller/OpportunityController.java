package org.launchpad.launchpad_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.common.SecurityHandler;
import org.launchpad.launchpad_backend.model.AccountRoleEnum;
import org.launchpad.launchpad_backend.model.Opportunity;
import org.launchpad.launchpad_backend.service.OpportunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.launchpad.launchpad_backend.common.ControllerUtils.controllerWrapper;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Validated
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final SecurityHandler securityHandler;

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

    @PostMapping
    public ResponseEntity<?> createNewOpportunity(@RequestHeader String authorizationToken,
                                                  @RequestBody @Valid Opportunity opportunity) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> opportunityService.createNewOpportunity(opportunity),
                List.of(AccountRoleEnum.TALENT_ACQUISITION)
        );
    }

    // MODIFY operations

    // DELETE operations
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOpportunityById(@RequestHeader String authorizationToken,
                                                   @PathVariable String id) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> opportunityService.deleteOpportunityById(id),
                List.of(AccountRoleEnum.TALENT_ACQUISITION)
        );
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllOpportunities() {
        return controllerWrapper(opportunityService::deleteAllOpportunities);
    }

}
