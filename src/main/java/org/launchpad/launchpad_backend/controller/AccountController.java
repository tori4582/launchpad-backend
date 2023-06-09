package org.launchpad.launchpad_backend.controller;

import lombok.RequiredArgsConstructor;
import org.launchpad.launchpad_backend.common.SecurityHandler;
import org.launchpad.launchpad_backend.model.Account;
import org.launchpad.launchpad_backend.model.request.AuthenticationRequestEntity;
import org.launchpad.launchpad_backend.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.launchpad.launchpad_backend.common.ControllerUtils.controllerWrapper;
import static org.launchpad.launchpad_backend.common.SecurityHandler.ALLOW_STAKEHOLDERS;
import static org.launchpad.launchpad_backend.model.AccountRoleEnum.ADMIN;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SecurityHandler securityHandler;

    // READ operations

    @GetMapping("/issue-rspwmail")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        return controllerWrapper(() -> accountService.issueResetPasswordMail(email));
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestParam String resetCredential,
                                                @RequestBody String resetToken) {
        return controllerWrapper(() -> accountService.validateResetToken(resetCredential, resetToken));
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                accountService::getAllAccounts,
                List.of(ADMIN)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAccountsByIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
                                                   @RequestParam String q) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> accountService.searchAccountByIdentity(q),
                ALLOW_STAKEHOLDERS
        );
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Account>> searchAccountsByDisplayName(@RequestParam String q) {
        return ResponseEntity.ok(accountService.searchAccountsByDisplayName(q));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestEntity reqEntity) {
        return controllerWrapper(() -> accountService.login(reqEntity));
    }

//    @PostMapping("/oauth2/login")
//    public ResponseEntity<?> loginViaOAuth2Provider(@RequestBody OAuth2AuthenticationRequestEntity reqEntity) {
//        return controllerWrapper(() -> accountService.loginViaOAuth2Provider(reqEntity));
//    }

    // WRITE operation

    @PostMapping("/signup")
    public ResponseEntity<?> createNewAccount(@RequestBody Account reqEntity) {
        return controllerWrapper(() -> accountService.createNewAccount(reqEntity));
    }

    // MODIFY operation

    @PostMapping("/{identity}")
    public ResponseEntity<?> updateExistingAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
                                                    @PathVariable String identity,
                                                    @RequestBody Account reqEntity) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> accountService.updateExistingAccount(identity, reqEntity),
                ALLOW_STAKEHOLDERS
        );
    }

    @PostMapping("/{identity}/{fieldName}")
    public ResponseEntity<?> updateFieldValueOfExistingAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
                                                                @PathVariable String identity,
                                                                @PathVariable String fieldName,
                                                                @RequestBody Object newValue) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> accountService.updateFieldValueOfExistingAccount(identity, fieldName, newValue),
                ALLOW_STAKEHOLDERS
        );
    }

    @PostMapping("/{identity}/role")
    public ResponseEntity<?> changeRoleOfExistingAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
                                                      @PathVariable String identity,
                                                      @RequestBody String newRole) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> accountService.changeRoleOfExistingAccount(identity, newRole),
                List.of(ADMIN)
        );
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetNewPasswordForExistingAccount(@RequestParam String resetCredential,
                                                             @RequestBody String newHashedPassword) {
        return controllerWrapper(() -> accountService.resetNewPasswordForExistingAccount(resetCredential, newHashedPassword));
    }

//    @PostMapping("/oauth2/link")
//    public ResponseEntity<?> linkAccountWithAssociatedProvider(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
//                                                               @RequestBody OAuth2AuthenticationRequestEntity reqEntity) {
//        return securityHandler.roleGuarantee(
//                authorizationToken,
//                () -> accountService.linkAccountWithAssociatedProvider(reqEntity),
//                ALLOW_STAKEHOLDERS
//        );
//    }

//    @PostMapping("/oauth2/unlink")
//    public ResponseEntity<?> unlinkAccountWithAssociatedProvider(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
//                                                                 @RequestParam String userId,
//                                                                 @RequestParam String providerName) {
//        return securityHandler.roleGuarantee(
//                authorizationToken,
//                () -> accountService.unlinkAccountWithAssociatedProvider(userId, providerName),
//                ALLOW_STAKEHOLDERS
//        );
//    }

    // DELETE operation

    @DeleteMapping("/dev/all")
    public ResponseEntity<?> removeAllAccounts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                accountService::removeAllAccounts,
                List.of(ADMIN)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAccountById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationToken,
                                            @PathVariable String id) {
        return securityHandler.roleGuarantee(
                authorizationToken,
                () -> accountService.removeAccountById(id),
                ALLOW_STAKEHOLDERS
        );
    }

}
