package org.launchpad.launchpad_backend.service;

import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.config.aop.MultipleTransformToResponseEntities;
import org.launchpad.launchpad_backend.config.aop.TransformToResponseEntity;
import org.launchpad.launchpad_backend.config.aop.Transformable;
import org.launchpad.launchpad_backend.model.Opportunity;
import org.launchpad.launchpad_backend.repository.OpportunityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;

    @MultipleTransformToResponseEntities
    public List<? extends Transformable> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    @TransformToResponseEntity
    public Transformable getOpportunityById(String id) {
        return opportunityRepository.findById(id).orElseThrow();
    }

    @MultipleTransformToResponseEntities
    public List<? extends Transformable> searchOpportunitiesByCriteria(String titleQuery, List<String> tagsQuery, String positionQuery) {
        List<Opportunity> result = new ArrayList<>();

        result.addAll(opportunityRepository.findAllByJobTitleContainingIgnoreCase(titleQuery));
        result.addAll(opportunityRepository.findAllByPositionTitleContainingIgnoreCase(positionQuery));

        return result;
    }

    @TransformToResponseEntity
    public Transformable createNewOpportunity(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    @TransformToResponseEntity
    public Transformable deleteOpportunityById(String id) {
        var loadedEntity = this.getOpportunityById(id);
        opportunityRepository.deleteById(id);
        return loadedEntity;
    }

    public Long deleteAllOpportunities() {
        var result = opportunityRepository.count();
        opportunityRepository.deleteAll();
        return result;
    }
}
