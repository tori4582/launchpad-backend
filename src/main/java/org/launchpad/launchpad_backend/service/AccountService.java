package org.launchpad.launchpad_backend.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.launchpad.launchpad_backend.common.JwtUtils;
import org.launchpad.launchpad_backend.model.Account;
import org.launchpad.launchpad_backend.model.AccountProviderEnum;
import org.launchpad.launchpad_backend.model.AccountRoleEnum;
import org.launchpad.launchpad_backend.model.request.AuthenticationRequestEntity;
import org.launchpad.launchpad_backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.Optional.ofNullable;
import static org.launchpad.launchpad_backend.common.CommonUtils.getOrDefault;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final MailService mailService;
    private final SimpleInternalCredentialService simpleInternalCredentialService;

    public AccountService(@Value("${server.endpoint}") String resetPasswordEndpoint,
                          @Autowired AccountRepository accountRepositoryy,
                          @Autowired MailService mailService,
                          @Autowired SimpleInternalCredentialService simpleInternalCredentialService
                       ) {
        this.accountRepository = accountRepositoryy;
        this.mailService = mailService;
        this.simpleInternalCredentialService = simpleInternalCredentialService;
    }


    /* Test only */
    public long removeAllAccounts() {
        long quantity = accountRepository.count();
        accountRepository.deleteAll();
        return quantity;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account searchAccountByIdentity(String q) {
        return Optional.ofNullable(accountRepository.findByEmailIgnoreCase(q))
                .or(() -> Optional.ofNullable(accountRepository.findByUsernameIgnoreCase(q)))
                .orElse(null);
    }

    public List<Account> searchAccountsByDisplayName(String displayName) {
        return accountRepository.findAllByDisplayNameContainingIgnoreCase(displayName);
    }

    public Object createNewAccount(Account reqEntity) {

        if (Objects.nonNull(this.searchAccountByIdentity(reqEntity.getEmail()))) {
            throw new IllegalArgumentException(String.format("Account with given email '%s' is already exists", reqEntity.getEmail()));
        }
        if (Objects.nonNull(this.searchAccountByIdentity(reqEntity.getUsername()))) {
            throw new IllegalArgumentException(String.format("Account with given username '%s' is already exists", reqEntity.getUsername()));
        }

        reqEntity.setIsMale((Boolean) getOrDefault(reqEntity.getIsMale(), true));
        reqEntity.setAccountRole((AccountRoleEnum) getOrDefault(reqEntity.getAccountRole(), AccountRoleEnum.JOB_SEEKER));
        reqEntity.setAssociatedProviders(new LinkedHashMap<>());

        Account persistedAccount = accountRepository.save(reqEntity);
        persistedAccount.getAssociatedProviders().put(AccountProviderEnum.SELF_PROVIDED, persistedAccount.getId());

        return accountRepository.save(persistedAccount);
    }

//    public Account linkAccountWithAssociatedProvider(OAuth2AuthenticationRequestEntity reqEntity) {
//
//        Account loadedAccount = Objects.requireNonNull(
//                this.searchAccountByIdentity(reqEntity.getOauth2ProviderAccountIdentity()),
//                LOG_OAUTH2_INVALID_USER_IDENTITY
//        );
//
//        Account.AccountProvider oauth2Provider = Account.AccountProvider.valueOf(reqEntity.getOauth2ProviderProviderName().toUpperCase());
//        String associatedAccountId = loadedAccount.getAssociatedProviders().getOrDefault(oauth2Provider, null);
//        if (Objects.nonNull(associatedAccountId)) {
//            throw new IllegalArgumentException("Already linked account, invalid request. Please consider to unlink associated provider before renewing it.");
//        }
//
//        loadedAccount.getAssociatedProviders().put(oauth2Provider, reqEntity.getOauth2ProviderAccountId());
//
//        return accountRepository.save(loadedAccount);
//    }

//    public Account unlinkAccountWithAssociatedProvider(String userId, String associatedProviderName) {
//        Account loadedAccount = Objects.requireNonNull(this.getAccountById(userId), LOG_OAUTH2_INVALID_USER_IDENTITY);
//
//        Account.AccountProvider oauth2Provider = Account.AccountProvider.valueOf(associatedProviderName);
//        Objects.requireNonNull(
//                loadedAccount.getAssociatedProviders().getOrDefault(oauth2Provider, null),
//                LOG_OAUTH2_INVALID_USER_IDENTITY
//        );
//
//        loadedAccount.getAssociatedProviders().remove(oauth2Provider);
//        return accountRepository.save(loadedAccount);
//    }

//    public Map<String, Object> loginViaOAuth2Provider(OAuth2AuthenticationRequestEntity reqEntity) {
//        Account loadedAccount = Objects.requireNonNull(
//                this.searchAccountByIdentity(reqEntity.getOauth2ProviderAccountIdentity()),
//                LOG_OAUTH2_INVALID_USER_IDENTITY
//        );
//
//        Account.AccountProvider oauth2Provider = Account.AccountProvider.valueOf(reqEntity.getOauth2ProviderProviderName().toUpperCase());
//        String associatedAccountId = loadedAccount.getAssociatedProviders().getOrDefault(oauth2Provider, null);
//
//        if (Objects.isNull(associatedAccountId) || !associatedAccountId.equals(reqEntity.getOauth2ProviderAccountId())) {
//            throw new NullPointerException("Invalid persisted associated userId. This could be caused since the user is not linked with any OAuth2 provider.");
//        }
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("accessToken", JwtUtils.issueAuthenticatedAccessToken(loadedAccount));
//        result.put("userDocumentEntity", loadedAccount);
//
//        return result;
//    }

    public Map<String, Object> login(AuthenticationRequestEntity reqEntity) {
        Account loadedAccount = this.searchAccountByIdentity(reqEntity.getLoginIdentity());

        if (Objects.isNull(loadedAccount) || !loadedAccount.getHashedPassword().equals(reqEntity.getHashedPassword())) {
            throw new NullPointerException("Invalid username or password");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", JwtUtils.issueAuthenticatedAccessToken(loadedAccount));
        result.put("userDocumentEntity", loadedAccount);

        return result;
    }

    public Object removeAccountById(String id) {
        return accountRepository.findById(id)
                .map(loadedEntity -> {
                    accountRepository.delete(loadedEntity);
                    return loadedEntity;
                }).orElseThrow();
    }

    public Account updateExistingAccount(String identity, Account reqEntity) {
        Account preparedProduct = ofNullable(this.searchAccountByIdentity(identity))
                .map(loadedEntity -> {
                    loadedEntity.setUsername(reqEntity.getUsername());
                        loadedEntity.setDisplayName(reqEntity.getDisplayName());
                    loadedEntity.setIsMale((Boolean) getOrDefault(reqEntity.getIsMale(), true));
                    loadedEntity.setEmail(reqEntity.getEmail());
                    loadedEntity.setPhoneNumber(reqEntity.getPhoneNumber());
                    loadedEntity.setHashedPassword(reqEntity.getHashedPassword());
                    loadedEntity.setAvatarImageUrl(reqEntity.getAvatarImageUrl());
                    loadedEntity.setAccountRole((AccountRoleEnum) getOrDefault(reqEntity.getAccountRole(), AccountRoleEnum.JOB_SEEKER));
                    loadedEntity.setDateOfBirth(reqEntity.getDateOfBirth());

                    return loadedEntity;
                }).orElseThrow();

        return accountRepository.save(preparedProduct);
    }

    @SneakyThrows
    public Account updateFieldValueOfExistingAccount(String identity, String fieldName, Object newValue) {

        final List<String> unmodifiableFields = List.of("userId", "userRole", "accountProvider", "hashedPassword");
        if (unmodifiableFields.contains(fieldName)) {
            throw new IllegalAccessException("Invalid action, unable to update unmodifiable field");
        }

        Account preparedAccount = Optional.ofNullable(this.searchAccountByIdentity(identity)).orElseThrow();

        Field preparedField = preparedAccount.getClass().getDeclaredField(fieldName);
        preparedField.setAccessible(true);

        log.info("Performs updating field value: "
                + identity + "::" + "Account$"
                + preparedField.getName() + "::" + preparedField.getType().getSimpleName()
                + " : " + preparedField.get(preparedAccount)
                + " => " + newValue
        );

        preparedField.set(preparedAccount, newValue);

        return accountRepository.save(preparedAccount);
    }

    @SneakyThrows
    public String issueResetPasswordMail(String emailAddress) {
        Account receiver = Optional.ofNullable(accountRepository.findByEmailIgnoreCase(emailAddress))
                .orElseThrow(() -> {throw new NullPointerException("Invalid email address: " + emailAddress);});

        SimpleInternalCredentialService.ResetRequestCredential credentialEntry = this.simpleInternalCredentialService.issueAndPersistResetCredential(receiver.getUsername());

        mailService.issueResetPassword(receiver, credentialEntry.getResetToken());

        return credentialEntry.getResetCredential();
    }

    public Account resetNewPasswordForExistingAccount(String resetCredential, String newHashedPassword) {
        String userEmail = simpleInternalCredentialService.acceptResetCredential(resetCredential);
        Account preparedAccount = this.searchAccountByIdentity(userEmail);

        preparedAccount.setHashedPassword(newHashedPassword);

        return accountRepository.save(preparedAccount);
    }

    public Account changeRoleOfExistingAccount(String identity, String newRole) {
        Account preparedAccount = Optional.ofNullable(this.searchAccountByIdentity(identity)).orElseThrow();

        preparedAccount.setAccountRole(AccountRoleEnum.valueOf(newRole));

        return accountRepository.save(preparedAccount);
    }

    public boolean validateResetToken(String resetCredential, String resetToken) {
        return simpleInternalCredentialService.isValid(resetCredential, resetToken);
    }
}
