package org.launchpad.launchpad_backend.repository;

import org.launchpad.launchpad_backend.model.NewsletterRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "newsletterRegistration")
public interface NewsletterRegistrationRepository extends MongoRepository<NewsletterRegistration, String> {

    List<NewsletterRegistration> findAll();

    NewsletterRegistration save(NewsletterRegistration newsletterRegistration);

}
