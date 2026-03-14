package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.common.PaginatedResponse;
import edu.uni.edumatrix.dto.common.Response;
import edu.uni.edumatrix.dto.user.*;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.service.UserService;
import edu.uni.edumatrix.util.annotations.RequirePrivileges;
import edu.uni.edumatrix.util.constants.HeaderTypes;
import edu.uni.edumatrix.util.constants.TokenTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import edu.uni.edumatrix.util.services.PrivilegeLoader;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final PrivilegeLoader privilegeLoader;

    public AuthController(UserService userService,
                          PrivilegeLoader privilegeLoader) {
        this.userService = userService;
        this.privilegeLoader = privilegeLoader;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDTO> signIn(@Valid @RequestBody UserSignInDTO userSignInRequest) {
        Response<User> userResponse = userService.signIn(userSignInRequest);
        if (userResponse.getData().getStatus().equals(UserStatus.USER_STATUS_INACTIVE)) {
            throw new BadRequestException("User is inactive");
        }
        return ResponseEntity.ok()
                .header(HeaderTypes.AUTHORIZATION, userResponse.getMetaData().get(HeaderTypes.AUTHORIZATION))
                .header(HeaderTypes.REFRESH_TOKEN, userResponse.getMetaData().get(HeaderTypes.REFRESH_TOKEN))
                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
    }

    @PostMapping("/account-verify-request")
    public ResponseEntity<Void> accountVerifyRequest() {
        Response<User> userResponse = userService.accountActivationRequest();

        return ResponseEntity.ok()
                .header(HeaderTypes.ACCOUNT_VERIFICATION_TOKEN,
                        userResponse.getMetaData().get(TokenTypes.ACCOUNT_VERIFICATION_TOKEN))
                .build();
    }

    @PostMapping("/account-verify")
    public ResponseEntity<Void> accountVerify(@Valid @RequestBody AccountVerifyDTO accountVerifyRequest) {
        Response<User> userResponse = userService.verifyAccount(accountVerifyRequest);
        return ResponseEntity.ok()
                .header(HeaderTypes.PASSWORD_RESET_TOKEN,
                        userResponse.getMetaData().get(TokenTypes.PASSWORD_RESET_TOKEN))
                .build();
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Map<String, String>> passwordReset(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
        Response<User> userResponse = userService.passwordReset(passwordResetDTO);

        Map<String, String> responseBody = Map.of("status", userResponse.getData().getStatus());
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping("/password-change")
    public ResponseEntity<UserDTO> passwordChange(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        Response<User> userResponse = userService.passwordChange(passwordChangeDTO);
        return ResponseEntity.ok()
                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestParam(value = "email") String email) {
        Response<User> userResponse = userService.forgotPassword(email);
        return ResponseEntity.ok()
                .header(HeaderTypes.ACCOUNT_VERIFICATION_TOKEN,
                        userResponse.getMetaData().get(TokenTypes.ACCOUNT_VERIFICATION_TOKEN))
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<UserDTO> processRefreshToken(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshTokenHeader,
            @RequestParam(value = "_refreshToken", required = false) String refreshToken) {
        Response<User> userResponse = userService.processRefreshToken(refreshTokenHeader == null ? refreshToken : refreshTokenHeader);
        return ResponseEntity.ok()
                .header(HeaderTypes.AUTHORIZATION, userResponse.getMetaData().get(HeaderTypes.AUTHORIZATION))
                .header(HeaderTypes.REFRESH_TOKEN, userResponse.getMetaData().get(HeaderTypes.REFRESH_TOKEN))
                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
    }
//
//    @PutMapping("/inactivate/{id}")
//    @RequirePrivileges({"USER_MANAGEMENT.STATUS_CHANGE"})
//    public ResponseEntity<UserDTO> inactivateUser(@PathVariable String id) {
//        Response<User> userResponse = userService.inactivate(id);
//        return ResponseEntity.ok()
//                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
//    }
//
//    @PutMapping("/activate/{id}")
//    @RequirePrivileges({"USER_MANAGEMENT.STATUS_CHANGE"})
//    public ResponseEntity<UserDTO> activateUser(@PathVariable String id) {
//        Response<User> userResponse = userService.activate(id);
//        return ResponseEntity.ok()
//                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
//    }
//

    @GetMapping("/current-user")
    public ResponseEntity<UserDTO> getCurrentUser() {
        User user = userService.getSessionUser();
        return ResponseEntity.ok(UserDTO.init(user, privilegeLoader.getGroups()));
    }

//
//    @PutMapping("/resend-invitation")
//    @RequirePrivileges({"USER_MANAGEMENT.CREATE"})
//    public ResponseEntity<UserDTO> resendInvitation(@Valid @RequestBody CreateUserDTO createUserDTO) {
//        Response<User> userResponse = userService.resendInvitation(createUserDTO.getEmail());
//        return ResponseEntity.ok().body(
//                UserDTO.init(userResponse.getData(), privilegeLoader.getGroups())
//        );
//    }

}
