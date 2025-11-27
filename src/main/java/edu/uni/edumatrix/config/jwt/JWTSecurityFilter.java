package edu.uni.edumatrix.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import edu.uni.edumatrix.config.GlobalConfig;
import edu.uni.edumatrix.config.Session;
import edu.uni.edumatrix.model.User;
import edu.uni.edumatrix.service.UserService;
import edu.uni.edumatrix.util.commons.RequestDataProvider;
import edu.uni.edumatrix.util.constants.TokenTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.exceptions.custom.TokenInvalidException;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JWTSecurityFilter extends BasicAuthenticationFilter {
    private final UserService userService;
    private final GlobalConfig globalConfig;
    private final RequestDataProvider requestDataProvider;
    public JWTSecurityFilter(
            UserService userService,
            GlobalConfig globalConfig,
            RequestDataProvider requestDataProvider, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.userService = userService;
        this.globalConfig = globalConfig;
        this.requestDataProvider = requestDataProvider;
    }

    public static final List<String> EXACT_MATCH_ENDPOINTS = List.of(
            "/users/sign-in",
            "/users/refresh-token",
            "/users/forgot-password",
            "/users/account-verify",
            "/users/password-reset",
            "/health/status"
    );

    public static final List<String> PREFIX_MATCH_ENDPOINTS = List.of(
            "/swagger-ui/",
            "/v3/"
    );

    public static final List<String> ACTIVATION_NOT_REQUIRED_ENDPOINTS = List.of(
            "/api/users/account-verify-request",
            "/api/users/current-user"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String servletPrefix = request.getServletPath();
        String path = request.getRequestURI().substring(servletPrefix.length());
        if (EXACT_MATCH_ENDPOINTS.contains(path) ||
                PREFIX_MATCH_ENDPOINTS.stream().anyMatch(path::startsWith)) {
            log.info("Public endpoint, skipping authentication : {}", path);
            chain.doFilter(request, response);
            return;
        }
        MDC.put("request_unique_id", requestDataProvider.getRequestHash());
        String token = request.getHeader("Authorization");
        if (token == null) {
            token = request.getParameter("_authorization");
        }
        if (token == null) {
            log.error("token not found in the request");
            chain.doFilter(request, response);
            return;
        }
        try {
            JWTContent jwtContent = JWT.decode(token, globalConfig.getJwtSecret());
            log.debug("jwt token decoded, user = {}", jwtContent.getSubject());
            if (!jwtContent.getPayload().get("type").equals(TokenTypes.ACCESS_TOKEN)) {
                log.error("invalid token type: {}", jwtContent.getPayload().get("type"));
                throw new TokenInvalidException("Unauthorized");
            }
            User user = this.userService.findById(jwtContent.getPayload().get("id"));
            String status = user.getStatus();
            if (ACTIVATION_NOT_REQUIRED_ENDPOINTS.contains(request.getRequestURI())) {
                log.info("Activation not required for this endpoint : {}", request.getRequestURI());
                Session.setUser(user);
                chain.doFilter(request, response);
                return;
            }

            if (!UserStatus.USER_STATUS_ACTIVE.equals(status)) {
                log.error("User status is not ACTIVE: {}", status);
                throw new BadRequestException("User is inactive");
            }
            // update request hash
            requestDataProvider.setRequestHash(requestDataProvider.getRequestHash() + "-" +  user.getEmail());
            MDC.put("request_unique_id", requestDataProvider.getRequestHash());
            Session.setUser(user);
        } catch (TokenExpiredException exp) {
            log.info("token expired = {}", exp.getMessage());
            request.setAttribute("tok-expired", true);
        } catch (BadRequestException exp) {
            log.error("Bad request: {}", exp.getMessage());
            request.setAttribute("user-inactive", true);
        }catch (Exception e) {
            log.error("Error logging in: {}", e.getMessage());
            throw new TokenInvalidException("Unauthorized");
        }
        chain.doFilter(request, response);
    }
}
