package org.launchpad.launchpad_backend.config.aop;

import org.launchpad.launchpad_backend.model.AccountRoleEnum;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRoles {
    AccountRoleEnum[] value();
}
