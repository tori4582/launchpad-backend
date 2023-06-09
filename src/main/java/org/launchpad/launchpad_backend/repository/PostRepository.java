package org.launchpad.launchpad_backend.repository;

import org.launchpad.launchpad_backend.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "post", path = "posts")
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAll();
    Optional<Post> findById(@Param("id") String id);

}
