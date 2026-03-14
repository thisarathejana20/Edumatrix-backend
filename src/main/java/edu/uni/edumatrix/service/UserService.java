package edu.uni.edumatrix.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import edu.uni.edumatrix.config.GlobalConfig;
import edu.uni.edumatrix.config.Session;
import edu.uni.edumatrix.config.jwt.JWT;
import edu.uni.edumatrix.config.jwt.JWTContent;
import edu.uni.edumatrix.dto.common.Response;
import edu.uni.edumatrix.dto.user.AccountVerifyDTO;
import edu.uni.edumatrix.dto.user.PasswordChangeDTO;
import edu.uni.edumatrix.dto.user.PasswordResetDTO;
import edu.uni.edumatrix.dto.user.UserSignInDTO;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.commons.Generator;
import edu.uni.edumatrix.util.constants.*;
import edu.uni.edumatrix.util.exceptions.ExType;
import edu.uni.edumatrix.util.exceptions.custom.*;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import edu.uni.edumatrix.util.exceptions.http.NotFoundException;
import edu.uni.edumatrix.util.exceptions.http.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static edu.uni.edumatrix.util.constants.RoleTypes.ROOT_ADMIN;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GlobalConfig globalConfig;
    private static final String AUTH_FAILED_MESSAGE = "Authentication failed";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    @Value("${spring.profiles.active}")
    private String environment;

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
        if (user.isHasSystemAccess() && !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
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

    public Response<User> accountActivationRequest() {
        log.info("Account Activation Request for user");
        User user = Session.getUser();
        if (!user.getStatus().equals(UserStatus.USER_STATUS_PENDING_ACTIVATION)) {
            throw new BadRequestException("Account is not pending activation");
        }
        return generateOTPToken(user);
    }

    private Response<User> generateOTPToken(User user) {
        log.info("Generate OTP Token for user: {}", user.getId());
        String pin;
        if (EnvironmentTypes.DEVELOPMENT.equals(environment) || EnvironmentTypes.STAGE.equals(environment) ||
                EnvironmentTypes.LOCAL.equals(environment)) {
            pin = ExtraConstants.MOCK_OTP;
        } else {
            pin = Generator.generateRandomNumber(6);
        }
        user.setOtp(passwordEncoder.encode(pin));
        userRepository.save(user);
        JWTContent authContent = JWTContent.builder()
                .subject(user.getId())
                .payload(Map.of("id", user.getId(),
                        "type", TokenTypes.ACCOUNT_VERIFICATION_TOKEN))
                .expiredIn(globalConfig.getAccountVerificationTokenExpiration())
                .build();
        Response<User> userResponse = Response.of(user);
        userResponse.setMetaData(Map.of(TokenTypes.ACCOUNT_VERIFICATION_TOKEN,
                JWT.encode(authContent, globalConfig.getJwtSecret())));
        return userResponse;
    }

    public Response<User> verifyAccount(AccountVerifyDTO accountVerifyRequest) {
        log.info("Verify Account Request for user");
        JWTContent decoded;
        try {
            decoded = JWT.decode(accountVerifyRequest.getToken(), globalConfig.getJwtSecret());
        } catch (JWTVerificationException e) {
            if (e.getMessage().contains("expired"))
                throw new UtilityTokenExpiredException(ExType.VERIFICATION_TOKEN_EXPIRED, AUTH_FAILED_MESSAGE);
            else throw new UtilityTokenInvalidException(ExType.VERIFICATION_TOKEN_INVALID, AUTH_FAILED_MESSAGE);
        }
        if (decoded.getPayload().get("type").equals(TokenTypes.ACCOUNT_VERIFICATION_TOKEN)) {
            User user = userRepository.findById(decoded.getSubject()).orElseThrow(() ->
                    new InvalidCredentialsException(AUTH_FAILED_MESSAGE));
            if (passwordEncoder.matches(accountVerifyRequest.getOtp(), user.getOtp())) {
                Response<User> userResponse = Response.of(user);
                userResponse.setMetaData(Map.of(TokenTypes.PASSWORD_RESET_TOKEN, generatePasswordResetToken(user)));
                return userResponse;
            } else {
                throw new InvalidCredentialsException(AUTH_FAILED_MESSAGE);
            }
        } else {
            throw new UtilityTokenInvalidException(ExType.VERIFICATION_TOKEN_INVALID, AUTH_FAILED_MESSAGE);
        }
    }

    public Response<User> passwordReset(PasswordResetDTO passwordResetRequest) {
        log.info("Password Reset Request for user");
        JWTContent decoded;
        try {
            decoded= JWT.decode(passwordResetRequest.getToken(), globalConfig.getJwtSecret());
        } catch (JWTVerificationException e) {
            if (e.getMessage().contains("expired"))
                throw new UtilityTokenExpiredException(ExType.PASSWORD_RESET_TOKEN_EXPIRED, AUTH_FAILED_MESSAGE);
            else throw new UtilityTokenInvalidException(ExType.PASSWORD_RESET_TOKEN_INVALID, AUTH_FAILED_MESSAGE);
        }
        if (decoded.getPayload().get("type").equals(TokenTypes.PASSWORD_RESET_TOKEN)) {
            User user = userRepository.findById(decoded.getSubject()).orElseThrow(() ->
                    new InvalidCredentialsException(AUTH_FAILED_MESSAGE));
            String newPassword = passwordResetRequest.getNewPassword();
            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new BadRequestException("New password cannot be same as old password");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setStatus(UserStatus.USER_STATUS_ACTIVE);
            userRepository.save(user);
            return Response.of(user);
        } else {
            log.error("Invalid token");
            throw new UtilityTokenInvalidException(ExType.PASSWORD_RESET_TOKEN_INVALID, AUTH_FAILED_MESSAGE);
        }
    }

    @Transactional
    public Response<User> passwordChange(PasswordChangeDTO passwordChangeDTO) {
        log.info("Password Change Request for user");
        User user = getSessionUser();
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException(AUTH_FAILED_MESSAGE);
        }
        String newPassword = passwordChangeDTO.getNewPassword();
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("New password cannot be same as old password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return Response.of(user);
    }

    public Response<User> forgotPassword(String email) {
        log.info("Forgot Password Request for email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new InvalidCredentialsException(AUTH_FAILED_MESSAGE));
        if (UserStatus.USER_STATUS_INACTIVE.equals(user.getStatus())) {
            throw new UnauthorizedException(ExType.USER_INACTIVE, "User is inactive");
        }
        return generateOTPToken(user);
    }

    public User findById(String id) {
        log.info("Find user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    public User getSessionUser() {
        User currentUser = Session.getUser();
        assert currentUser != null;
        return userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UnauthorizedException("No user is logged in"));
    }

//    private List<String> getValidPrivileges(List<String> requestedPrivileges) {
//        log.info("Get valid privileges for user");
//        Set<String> allowed = privilegeLoader.getAllPrivilegeNames();
//        List<String> sanitized = requestedPrivileges.stream()
//                .filter(allowed::contains)
//                .toList();
//        if (sanitized.isEmpty()) {
//            throw new BadRequestException("Invalid privileges");
//        }
//        return sanitized;
//    }

    private boolean isValidRole(String role) {
        return RoleTypes.EMPLOYEE.equalsIgnoreCase(role) ||
                RoleTypes.GUEST.equalsIgnoreCase(role);
    }

//    private Role validateAndGetRole(
//            String requestedRole,
//            List<String> privileges,
//            String currentRole,
//            User toBeUpdatedUser
//    ) {
//        log.info("Validate and get role: {}", requestedRole);
//
//        if (!isValidRole(requestedRole)) {
//            requestedRole = RoleTypes.EMPLOYEE;
//        }
//
//        String sessionUserRole = Session.getUser().getRole().getName();
//        boolean isRootAdmin = ROOT_ADMIN.equalsIgnoreCase(sessionUserRole);
//        boolean isHRAdmin = RoleTypes.HR_ADMIN.equalsIgnoreCase(sessionUserRole);
//
//        String normalizedRequestedRole = requestedRole.toUpperCase();
//        String normalizedCurrentRole = currentRole != null ? currentRole.toUpperCase() : null;
//
//        boolean isTargetRoleHRAdmin =
//                RoleTypes.HR_ADMIN.equalsIgnoreCase(normalizedRequestedRole);
//
//        boolean isRoleChange =
//                currentRole != null && !normalizedRequestedRole.equals(normalizedCurrentRole);
//
//        if (currentRole == null) {
//            if (isTargetRoleHRAdmin && !isRootAdmin) {
//                throw new BadRequestException(
//                        "You cannot create HR Admin users. Please contact the System Admin."
//                );
//            }
//
//        } else {
//            boolean isSelfUpdate = toBeUpdatedUser.getId().equals(Session.getUser().getId());
//            boolean isTargetUserHRAdmin = RoleTypes.HR_ADMIN.equalsIgnoreCase(normalizedCurrentRole);
//
//            // Check if HR Admin is trying to edit another HR Admin (regardless of role change)
//            if (isHRAdmin && !isSelfUpdate && isTargetUserHRAdmin) {
//                throw new BadRequestException(
//                        "You do not have permission to edit another HR Admin's profile."
//                );
//            }
//
//            // Check if HR Admin is trying to change their own role
//            if (isHRAdmin && isSelfUpdate && isRoleChange) {
//                throw new BadRequestException(
//                        "You cannot change your own role. Please contact the System Admin."
//                );
//            }
//
//            // Check if HR Admin is trying to promote someone to HR Admin
//            if (isHRAdmin && !isSelfUpdate && isRoleChange && isTargetRoleHRAdmin) {
//                throw new BadRequestException(
//                        "HR Admin cannot assign HR Admin role to others."
//                );
//            }
//        }
//
//        if (privileges != null &&
//                !authCheckService.hasAllPrivileges(
//                        List.of("USER_MANAGEMENT.PRIVILEGES_MANAGEMENT")
//                )) {
//            throw new AccessDeniedException(
//                    "User does not have required privileges"
//            );
//        }
//
//        return roleRepository.findByName(normalizedRequestedRole)
//                .orElseGet(() ->
//                        roleRepository.save(
//                                Role.builder()
//                                        .name(normalizedRequestedRole)
//                                        .build()
//                        )
//                );
//    }

//    public Response<User> editUser(EditUserDTO editUserDTO, String id) {
//        log.info("Edit user: {}", id);
//        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
//        List<String> privileges = editUserDTO.getPrivileges();
//        Role userRole = validateAndGetRole(
//                editUserDTO.getRole(),
//                privileges,
//                user.getRole() != null ? user.getRole().getName() : null,
//                user
//        );
//        if (user.getRole() != null && ROOT_ADMIN.equalsIgnoreCase(user.getRole().getName())) {
//            throw new BadRequestException("Root Admin user cannot be updated");
//        }
//        if (StringUtils.isNotBlank(editUserDTO.getFullName())) user.setFullName(editUserDTO.getFullName());
//        updateUserDetails(user, editUserDTO.getAddress(),
//                editUserDTO.getMobile(),
//                editUserDTO.getEmpId(),
//                editUserDTO.getDepartment(),
//                editUserDTO.getDesignation());
//        User reportingPerson = null;
//        if (StringUtils.isNotBlank(editUserDTO.getReportingPersonId())) {
//            reportingPerson = userRepository.findById(editUserDTO.getReportingPersonId())
//                    .orElseThrow(() -> new NotFoundException("Reporting Person not found"));
//
//            if (reportingPerson.getId().equals(user.getId())) {
//                throw new BadRequestException("Reporting Person cannot be same as the user");
//            }
//        }
//        List<User> subManagers = null;
//        if (editUserDTO.getSubReportingPersonIds() != null) {
//            subManagers = userRepository.findAllById(editUserDTO.getSubReportingPersonIds());
//
//            if (subManagers.contains(user)) {
//                throw new BadRequestException(
//                        "A user cannot be their own sub-reporting manager."
//                );
//            }
//        }
//        validateReportingAndSubReporting(reportingPerson, subManagers);
//        user.setReportingPerson(reportingPerson);
//        user.setSubReportingPersons(subManagers);
//
//        setDefaultPrivilegesForRole(privileges, userRole, user);
//        return Response.of(user);
//    }

    public Response<User> getUserById(String id) {
        log.info("Get user by ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MESSAGE));
        if (user.getRole() != null && ROOT_ADMIN.equalsIgnoreCase(user.getRole().getName())) {
            throw new BadRequestException("Root Admin user cannot be accessed");
        }
        return Response.of(user);
    }
}
