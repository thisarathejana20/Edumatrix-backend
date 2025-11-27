package edu.uni.edumatrix.config.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWT {
    private JWT() {}
    public static String encode(JWTContent jwtContent,String secret) {
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        if (jwtContent.getPayload() != null) {
            for (Map.Entry<String, String> entry : jwtContent.getPayload().entrySet()) {
                builder = builder.withClaim(entry.getKey(), entry.getValue());
            }
        }
        return builder.withSubject(jwtContent.getSubject())
                .withExpiresAt(DateUtils.addSeconds(new Date(), (int) jwtContent.getExpiredIn()))
                .sign(Algorithm.HMAC256(secret));
    }

    public static JWTContent decode(String token, String secret) {
        try {
            JWTContent jwtContent = JWTContent.builder().build();
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = com.auth0.jwt.JWT.require(algorithm).build();
            DecodedJWT content = verifier.verify(token);
            jwtContent.setSubject(content.getSubject());
            Map<String, String> claims = new HashMap<>();
            for (Map.Entry<String, Claim> entry : content.getClaims().entrySet()) {
                claims.put(entry.getKey(), entry.getValue().asString());
            }
            jwtContent.setPayload(claims);
            return jwtContent;
        } catch (JWTVerificationException exp) {
            log.error("cannot decode jwt token, error={}", exp.getMessage());
            throw exp;
        }
    }
}
