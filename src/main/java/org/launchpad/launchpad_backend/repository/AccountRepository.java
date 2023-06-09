package org.launchpad.launchpad_backend.repository;

import org.launchpad.launchpad_backend.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "account")
public interface AccountRepository extends MongoRepository<Account, String> {

    Account findAccountByUsername(@Param("username") String username);

    Account findAccountByEmail(@Param("email") String email);

    List<Account> findAll();

    List<Account> findAllByDisplayNameContainingIgnoreCase(String nameQuery);

    Account findByEmailIgnoreCase(String q);

    Account findByUsernameIgnoreCase(String q);
}
