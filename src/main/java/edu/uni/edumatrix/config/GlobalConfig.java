package edu.uni.edumatrix.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GlobalConfig {
    private final String jwtSecret;
    private final int authExpiration;
    private final int refreshExpiration;
    private final int accountVerificationTokenExpiration;
    private final int passwordResetTokenExpiration;

    public GlobalConfig(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.auth.expiration}") int authExpiration,
                        @Value("${jwt.refresh.expiration}") int refreshExpiration,
                        @Value("${jwt.account-verification.expiration}") int accountVerificationTokenExpiration,
                        @Value("${jwt.passwordreset.expiration}") int passwordResetTokenExpiration) {
        this.jwtSecret = secret;
        this.authExpiration = authExpiration;
        this.refreshExpiration = refreshExpiration;
        this.accountVerificationTokenExpiration = accountVerificationTokenExpiration;
        this.passwordResetTokenExpiration = passwordResetTokenExpiration;
    }
}
