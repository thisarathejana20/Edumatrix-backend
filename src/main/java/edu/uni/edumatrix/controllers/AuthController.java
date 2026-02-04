package edu.uni.edumatrix.controllers;

import edu.uni.edumatrix.dto.common.Response;
import edu.uni.edumatrix.dto.user.UserDTO;
import edu.uni.edumatrix.dto.user.UserSignInDTO;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.service.UserService;
import edu.uni.edumatrix.util.constants.HeaderTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.exceptions.http.BadRequestException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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
                .body(UserDTO.init(userResponse.getData()));
    }

//    @PostMapping("/account-verify-request")
//    public ResponseEntity<Void> accountVerifyRequest() {
//        Response<User> userResponse = userService.accountActivationRequest();
//
//        return ResponseEntity.ok()
//                .header(HeaderTypes.ACCOUNT_VERIFICATION_TOKEN,
//                        userResponse.getMetaData().get(TokenTypes.ACCOUNT_VERIFICATION_TOKEN))
//                .build();
//    }
//
//    @PostMapping("/account-verify")
//    public ResponseEntity<Void> accountVerify(@Valid @RequestBody AccountVerifyDTO accountVerifyRequest) {
//        Response<User> userResponse = userService.verifyAccount(accountVerifyRequest);
//        return ResponseEntity.ok()
//                .header(HeaderTypes.PASSWORD_RESET_TOKEN,
//                        userResponse.getMetaData().get(TokenTypes.PASSWORD_RESET_TOKEN))
//                .build();
//    }
//
//    @PostMapping("/password-reset")
//    public ResponseEntity<Map<String, String>> passwordReset(@Valid @RequestBody PasswordResetDTO passwordResetDTO) {
//        Response<User> userResponse = userService.passwordReset(passwordResetDTO);
//
//        Map<String, String> responseBody = Map.of("status", userResponse.getData().getStatus());
//        return ResponseEntity.ok(responseBody);
//    }
//
//    @PutMapping("/password-change")
//    public ResponseEntity<UserDTO> passwordChange(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
//        Response<User> userResponse = userService.passwordChange(passwordChangeDTO);
//        return ResponseEntity.ok()
//                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
//    }
//
//    @PostMapping("/forgot-password")
//    public ResponseEntity<Void> forgotPassword(@RequestParam(value = "email") String email) {
//        Response<User> userResponse = userService.forgotPassword(email);
//        return ResponseEntity.ok()
//                .header(HeaderTypes.ACCOUNT_VERIFICATION_TOKEN,
//                        userResponse.getMetaData().get(TokenTypes.ACCOUNT_VERIFICATION_TOKEN))
//                .build();
//    }
//
//    @PostMapping("/refresh-token")
//    @Operation(
//            summary = "Refresh token",
//            parameters = {
//                    @Parameter(name = "X-Refresh-Token",
//                            in = ParameterIn.HEADER, required = true, description = "Refresh token")
//            }
//    )
//    public ResponseEntity<UserDTO> processRefreshToken(
//            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshTokenHeader,
//            @RequestParam(value = "_refreshToken", required = false) String refreshToken) {
//        Response<User> userResponse = userService.processRefreshToken(refreshTokenHeader == null ? refreshToken : refreshTokenHeader);
//        return ResponseEntity.ok()
//                .header(HeaderTypes.AUTHORIZATION, userResponse.getMetaData().get(HeaderTypes.AUTHORIZATION))
//                .header(HeaderTypes.REFRESH_TOKEN, userResponse.getMetaData().get(HeaderTypes.REFRESH_TOKEN))
//                .body(UserDTO.init(userResponse.getData(), privilegeLoader.getGroups()));
//    }
//
//    @PostMapping("/create-user")
//    @RequirePrivileges({"USER_MANAGEMENT.CREATE"})
//    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
//        Response<User> userResponse = userService.createUser(createUserDTO);
//        return ResponseEntity.ok().body(
//                UserDTO.init(userResponse.getData(), privilegeLoader.getGroups())
//        );
//    }
//
//    @PutMapping("/edit-user/{id}")
//    @RequirePrivileges({"USER_MANAGEMENT.UPDATE"})
//    public ResponseEntity<UserDTO> editUser(@Valid @RequestBody EditUserDTO editUserDTO,
//                                            @PathVariable String id) {
//        Response<User> userResponse = userService.editUser(editUserDTO,id);
//        return ResponseEntity.ok().body(
//                UserDTO.init(userResponse.getData(), privilegeLoader.getGroups())
//        );
//    }
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
//    @GetMapping("")
//    @RequirePrivileges({"USER_MANAGEMENT.VIEW"})
//    public ResponseEntity<PaginatedResponse<List<UserDTO>>> getAllUsers(
//            @RequestParam(required = false) String role,
//            @RequestParam(required = false) String designation,
//            @RequestParam(required = false) String query,
//            @RequestParam(defaultValue = "0", name = "page") int page,
//            @RequestParam(defaultValue = "10", name = "size") int size) {
//        Page<User> users = userService.getAllUsers(role, designation, query, page, size);
//        PaginatedResponse<List<UserDTO>> listPaginatedResponse = PaginatedResponse.of(
//                UserDTO.init(users.getContent(), privilegeLoader.getGroups()));
//        listPaginatedResponse.setTotalPages(users.getTotalPages());
//        listPaginatedResponse.setTotalElements(users.getTotalElements());
//        listPaginatedResponse.setPage(users.getNumber());
//        listPaginatedResponse.setSize(users.getSize());
//        listPaginatedResponse.setLast(users.isLast());
//        return ResponseEntity.ok(listPaginatedResponse);
//    }
//
//    @GetMapping("/{id}")
//    @RequirePrivileges({"USER_MANAGEMENT.VIEW"})
//    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
//        Response<User> response = userService.getUserById(id);
//        UserDTO dto = UserDTO.init(response.getData(), privilegeLoader.getGroups());
//        return ResponseEntity.ok(dto);
//    }
//
//    @GetMapping("/unregistered-users")
//    @RequirePrivileges({"USER_MANAGEMENT.VIEW"})
//    public ResponseEntity<List<UnregisteredUserDTO>> getAllUsersFromGraph() {
//        return ResponseEntity.ok(userService.getAllUsersFromGraph());
//    }
//
//    @PostMapping("/bulk-create")
//    @RequirePrivileges({"USER_MANAGEMENT.CREATE"})
//    public ResponseEntity<List<UserDTO>> bulkCreateUsers(@RequestBody List<CreateUserDTO> users) {
//        List<User> created = userService.createSelectedUsers(users);
//        return ResponseEntity.ok(created.stream().map(user -> UserDTO.init(user, privilegeLoader.getGroups())).toList());
//    }
//
//    @GetMapping("/privileges")
//    public ResponseEntity<List<PrivilegeGroupDTO>> getPrivilegeGroups() {
//        return ResponseEntity.ok(userService.getAllPrivilegeGroups());
//    }
//
//    @GetMapping("/current-user")
//    public ResponseEntity<UserDTO> getCurrentUser() {
//        User user = userService.getSessionUser();
//        return ResponseEntity.ok(UserDTO.init(user, privilegeLoader.getGroups()));
//    }
//
//    @GetMapping("/employee-metadata")
//    public Map<String, Map<String, String>> getEmployeeMeta() {
//        return Map.of(
//                "departments", EmployeeMetaStore.getDepartments(),
//                "designations", EmployeeMetaStore.getDesignations()
//        );
//    }
//
//    @GetMapping("/search")
//    @RequirePrivileges({"USER_MANAGEMENT.VIEW"})
//    public ResponseEntity<List<UserDTO>> searchUsersForReportingPerson(@RequestParam(required = true) String query,
//                                                                       @RequestParam(required = true) String type) {
//        List<User> listOfUsers = userService.searchUsers(query);
//        List<UserDTO> dtoList;
//        if (type.equals("USER")) {
//            dtoList = listOfUsers.stream()
//                    .filter(user -> user.getRole() == null || (!ROOT_ADMIN.equalsIgnoreCase(user.getRole().getName())
//                            && !user.getDesignation().startsWith("Intern")))
//                    .map(user -> UserDTO.init(user, privilegeLoader.getGroups()))
//                    .toList();
//        } else {
//            dtoList = listOfUsers.stream()
//                    .filter(user -> user.getRole() == null || (!ROOT_ADMIN.equalsIgnoreCase(user.getRole().getName())))
//                    .map(user -> UserDTO.init(user, privilegeLoader.getGroups()))
//                    .toList();
//        }
//        return ResponseEntity.ok(dtoList);
//    }
//
//    @PostMapping("/import-excel")
//    @RequirePrivileges({"USER_MANAGEMENT.CREATE"})
//    public List<UserDTO> importUsers(
//            @RequestParam("file") MultipartFile file
//    ) {
//        return userService.createUsersFromExcel(file);
//    }
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
