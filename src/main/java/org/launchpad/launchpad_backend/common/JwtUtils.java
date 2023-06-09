package org.launchpad.launchpad_backend.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.launchpad.launchpad_backend.model.Account;

import java.util.Date;

public class JwtUtils {

    private static final long EXPIRE_DURATION_1_HOUR = 60 * 60 * 1000;

    public static String issueAuthenticatedAccessToken(Account documentUserEntity) {
        return Jwts.builder()
                .setSubject(documentUserEntity.getId() + "~" + documentUserEntity.getAccountRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION_1_HOUR))
                .compact();
    }

    public static DefaultClaims decodeJwtToken(String jwtToken) {
        return (DefaultClaims) Jwts.parserBuilder()
                .build()
                .parse(jwtToken)
                .getBody();
    }

}
