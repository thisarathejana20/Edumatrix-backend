package edu.uni.edumatrix.service.common;

import edu.uni.edumatrix.dto.user.PrivilegeDTO;
import edu.uni.edumatrix.dto.user.PrivilegeGroupDTO;
import edu.uni.edumatrix.dto.user.RolePrivilegeDTO;
import edu.uni.edumatrix.model.user.Role;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.user.RoleRepository;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.constants.EnvironmentTypes;
import edu.uni.edumatrix.util.constants.RoleTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.services.PrivilegeLoader;
import edu.uni.edumatrix.util.services.RolePrivilegeLoader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PrivilegeLoader privilegeLoader;
    private final RolePrivilegeLoader rolePrivilegeLoader;

    @Value("${spring.profiles.active}")
    private String environment;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           PrivilegeLoader privilegeLoader,
                           RolePrivilegeLoader rolePrivilegeLoader) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.privilegeLoader = privilegeLoader;
        this.rolePrivilegeLoader = rolePrivilegeLoader;
    }

    @PostConstruct
    public void init() {
        String email;
        log.info("Initialize data...");
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream adminInput = getClass().getResourceAsStream("/data/admin.json");
            InputStream inputStream = getClass().getResourceAsStream("/data/privileges.json");
            InputStream inputStreamForRoles = getClass().getResourceAsStream("/data/defaultPrivilegesForRoles.json");

            if (adminInput == null) {
                throw new IllegalStateException("admin.json not found!");
            }

            if (inputStream == null) {
                throw new IllegalStateException("Privileges.json not found!");
            }

            if (inputStreamForRoles == null) {
                throw new IllegalStateException("defaultPrivilegesForRoles.json not found!");
            }

            Map<String, String> admin = mapper.readValue(adminInput, Map.class);

            List<PrivilegeGroupDTO> groups = Arrays.asList(
                    mapper.readValue(inputStream, PrivilegeGroupDTO[].class)
            );

            List<String> allPrivilegeNames = new ArrayList<>();
            for (PrivilegeGroupDTO group : groups) {
                for (PrivilegeDTO privilegeDTO : group.getPrivileges()) {
                    allPrivilegeNames.add(privilegeDTO.getName());
                }
            }
            privilegeLoader.loadPrivileges(allPrivilegeNames, groups);
            log.info("Privileges loaded.");

            List<RolePrivilegeDTO> rolePrivilegeList = Arrays.asList(
                    mapper.readValue(inputStreamForRoles, RolePrivilegeDTO[].class)
            );
            rolePrivilegeLoader.loadRolePrivileges(rolePrivilegeList);

            if (EnvironmentTypes.PRODUCTION.equals(environment)) {
                email = admin.get("email-prod");
            } else {
                email = admin.get("email-dev");
            }
            if (userRepository.findByEmail(email).isPresent()) {
                return;
            }

            Role adminRole = roleRepository.findByName(RoleTypes.ROOT_ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(RoleTypes.ROOT_ADMIN).build()));

            User adminUser = User.builder()
                    .fullName("Admin")
                    .empId("ADMIN-123")
                    .address("Colombo")
                    .email(email)
                    .role(adminRole)
                    .password(passwordEncoder.encode(admin.get("password")))
                    .status(UserStatus.USER_STATUS_PENDING_ACTIVATION)
                    .privileges(rolePrivilegeLoader.getRolePrivileges().get(RoleTypes.ROOT_ADMIN))
                    .build();

            if (userRepository.findByEmail(adminUser.getEmail()).isEmpty()) userRepository.save(adminUser);
            log.info("Root admin user initialized.");
        } catch (Exception e) {
            log.error("Error initializing data: ", e);
        }
    }
}

