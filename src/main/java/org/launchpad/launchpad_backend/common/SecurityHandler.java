package org.launchpad.launchpad_backend.common;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.launchpad.launchpad_backend.model.AccountRoleEnum;
import org.launchpad.launchpad_backend.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.launchpad.launchpad_backend.common.ControllerUtils.controllerWrapper;
import static org.launchpad.launchpad_backend.common.ExceptionLogger.logInvalidAction;

@Component
@RequiredArgsConstructor
public class SecurityHandler {
    private final AccountRepository accountRepository;

    public static final List<AccountRoleEnum> ALLOW_STAKEHOLDERS = List.of(
            AccountRoleEnum.ADMIN,
            AccountRoleEnum.BOTH_ROLES,
            AccountRoleEnum.TALENT_ACQUISITION,
            AccountRoleEnum.JOB_SEEKER
    );

    @SneakyThrows
    public void roleGuaranteeForAop(String authenticationToken,
                              List<AccountRoleEnum> allowedRoles) {
        innerChecks(authenticationToken, allowedRoles);
    }

    @SneakyThrows
    private void innerChecks(String authenticationToken, List<AccountRoleEnum> allowedRoles) {
        DefaultClaims claims = JwtUtils.decodeJwtToken(authenticationToken.split(" ")[1]);

        Objects.requireNonNull(claims);

        String[] tokens = claims.getSubject().split("~");
        AccountRoleEnum requestAccountRole = AccountRoleEnum.valueOf(tokens[1]);

        if(!checkIfAccountIdIsExist(tokens[0]) || !checkIfRoleMatchesWithAccount(tokens[0], AccountRoleEnum.valueOf(tokens[1]))) {
            throw new IllegalArgumentException("Invalid user information");
        }

        if (!allowedRoles.contains(requestAccountRole)) {
            throw new IllegalAccessException("Account has insufficient privileges to perform this action");
        }
    }

    public ResponseEntity<?> roleGuarantee(String authenticationToken,
                                           Supplier<?> serviceExecutionSupplier,
                                           List<AccountRoleEnum> allowedRoles) {
        try {
            if (Strings.isBlank(authenticationToken)) {
                throw new NullPointerException("Action requires providing of the authentication token");
            }
        } catch (Exception e) {
            logInvalidAction(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }

        return controllerWrapper(serviceExecutionSupplier);
    }

    private boolean checkIfAccountIdIsExist(String userId) {
        return accountRepository.existsById(userId);
    }

    private boolean checkIfRoleMatchesWithAccount(String userId, AccountRoleEnum role) {
        return accountRepository.findById(userId)
                .map(user -> user.getAccountRole().equals(role))
                .orElseThrow(() -> new NullPointerException("Invalid user information"));
    }

}
