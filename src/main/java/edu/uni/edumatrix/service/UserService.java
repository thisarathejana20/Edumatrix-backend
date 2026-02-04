package edu.uni.edumatrix.service;

import edu.uni.edumatrix.config.GlobalConfig;
import edu.uni.edumatrix.config.jwt.JWT;
import edu.uni.edumatrix.config.jwt.JWTContent;
import edu.uni.edumatrix.dto.common.Response;
import edu.uni.edumatrix.dto.user.UserSignInDTO;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.constants.HeaderTypes;
import edu.uni.edumatrix.util.constants.TokenTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.custom.InvalidCredentialsException;
import edu.uni.edumatrix.util.exceptions.custom.TokenExpiredException;
import edu.uni.edumatrix.util.exceptions.custom.TokenInvalidException;
import edu.uni.edumatrix.util.exceptions.http.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GlobalConfig globalConfig;
    private static final String AUTH_FAILED_MESSAGE = "Authentication failed";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public  UserService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        GlobalConfig globalConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.globalConfig = globalConfig;
    }

    public Response<User> signIn(UserSignInDTO request) {
        log.info("User SignIn Request: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(AUTH_FAILED_MESSAGE);
        }
        Response<User> userResponse = Response.of(user);
        if (UserStatus.USER_STATUS_INACTIVE.equals(user.getStatus())) {
            throw new UnauthorizedException(ExType.USER_INACTIVE, "User is inactive");
        }
        userResponse.setMetaData(Map.of(HeaderTypes.AUTHORIZATION, generateAuthenticationTokens(user),
                HeaderTypes.REFRESH_TOKEN, generateRefreshToken(user)));
        return userResponse;
    }

    public Response<User> processRefreshToken(String token) {
        log.info("User Refresh Token Request");
        JWTContent decoded;
        try {
            decoded= JWT.decode(token, globalConfig.getJwtSecret());
        } catch (TokenExpiredException exp) {
            log.info("token expired = {}", exp.getMessage());
            throw new TokenExpiredException(ExType.REFRESH_TOKEN_EXPIRED, "Token is expired");
        } catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            throw new TokenInvalidException(ExType.REFRESH_TOKEN_INVALID, "Token is invalid");
        }
        if (!decoded.getPayload().get("type").equals(TokenTypes.REFRESH_TOKEN)) {
            throw new TokenInvalidException(ExType.REFRESH_TOKEN_INVALID, "Token type is invalid");
        }
        Optional<User> user = userRepository.findById(decoded.getPayload().get("id"));
        if (user.isEmpty()) {
            log.error("user not found for id = {}", decoded.getSubject());
            throw new InvalidCredentialsException(AUTH_FAILED_MESSAGE);
        }
        Response<User> userResponse = Response.of(user.get());
        userResponse.setMetaData(Map.of(HeaderTypes.AUTHORIZATION, generateAuthenticationTokens(user.get()),
                HeaderTypes.REFRESH_TOKEN, generateRefreshToken(user.get())));
        return userResponse;
    }

    private String generateAuthenticationTokens(User user) {
        log.info("Generate Authentication Tokens for user: {}", user.getId());
        JWTContent authContent = JWTContent.builder()
                .subject(user.getFullName())
                .payload(Map.of("id", user.getId(),
                        "type", TokenTypes.ACCESS_TOKEN))
                .expiredIn(globalConfig.getAuthExpiration())
                .build();
        return JWT.encode(authContent, globalConfig.getJwtSecret());
    }

    private String generateRefreshToken(User user) {
        log.info("Generate Refresh Token for user: {}", user.getId());
        JWTContent refreshContent = JWTContent.builder()
                .subject(user.getFullName())
                .payload(Map.of("id", user.getId(),
                        "type", TokenTypes.REFRESH_TOKEN))
                .expiredIn(globalConfig.getRefreshExpiration())
                .build();
        return JWT.encode(refreshContent, globalConfig.getJwtSecret());
    }

    public String generatePasswordResetToken(User user) {
        log.info("Generate Password Reset Token for user: {}", user.getId());
        JWTContent authContent = JWTContent.builder()
                .subject(user.getId())
                .payload(Map.of("id", user.getId(),
                        "type", TokenTypes.PASSWORD_RESET_TOKEN))
                .expiredIn(globalConfig.getPasswordResetTokenExpiration())
                .build();
        return JWT.encode(authContent, globalConfig.getJwtSecret());
    }

    public User findById(String id) {
        return null;
    }
}
