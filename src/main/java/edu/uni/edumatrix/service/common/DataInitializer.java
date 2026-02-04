package edu.uni.edumatrix.service.common;

import edu.uni.edumatrix.model.user.Role;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.user.RoleRepository;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.constants.EnvironmentTypes;
import edu.uni.edumatrix.util.constants.RoleTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.Map;

@Service
@Slf4j
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${spring.profiles.active}")
    private String environment;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        String email;
        log.info("Initialize data...");
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream adminInput = getClass().getResourceAsStream("/data/admin.json");

            if (adminInput == null) {
                throw new IllegalStateException("admin.json not found!");
            }

            Map<String, String> admin = mapper.readValue(adminInput, Map.class);

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
                    .build();

            if (userRepository.findByEmail(adminUser.getEmail()).isEmpty()) userRepository.save(adminUser);
            log.info("Root admin user initialized.");
        } catch (Exception e) {
            log.error("Error initializing data: ", e);
        }
    }
}

