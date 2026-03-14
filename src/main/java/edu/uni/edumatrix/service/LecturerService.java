package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.LecturerDTO;
import edu.uni.edumatrix.model.Department;
import edu.uni.edumatrix.model.Lecturer;
import edu.uni.edumatrix.model.user.Role;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.DepartmentRepository;
import edu.uni.edumatrix.repository.LecturerRepository;
import edu.uni.edumatrix.repository.user.RoleRepository;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.commons.Generator;
import edu.uni.edumatrix.util.constants.EnvironmentTypes;
import edu.uni.edumatrix.util.constants.ExtraConstants;
import edu.uni.edumatrix.util.constants.RoleTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LecturerService {

    private final LecturerRepository lecturerRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.profiles.active}")
    private String environment;

    @Transactional
    public LecturerDTO.Response createLecturer(
            LecturerDTO.CreateRequest request,
            boolean hasSystemAccess
    ) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        Role lecturerRole = roleRepository.findByName(RoleTypes.LECTURER)
                .orElseThrow(() ->
                        new RuntimeException("Role not configured"));

        if (hasSystemAccess) {
            String systemGeneratedPassword;
            if (EnvironmentTypes.DEVELOPMENT.equals(environment) || EnvironmentTypes.STAGE.equals(environment) ||
                    EnvironmentTypes.LOCAL.equals(environment) ) {
                systemGeneratedPassword = ExtraConstants.MOCK_PASSWORD;
            } else {
                systemGeneratedPassword = Generator.generatePassword();
            }

            user.setPassword(
                    passwordEncoder.encode(systemGeneratedPassword)
            );
            user.setStatus(UserStatus.USER_STATUS_PENDING_ACTIVATION);
            user.setHasSystemAccess(true);

        } else {
            user.setStatus(UserStatus.USER_STATUS_ACTIVE);
            user.setHasSystemAccess(false);
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(lecturerRole);

        user = userRepository.save(user);


        Department department =
                departmentRepository.findById(
                        request.getDepartmentId()
                ).orElseThrow(() ->
                        new RuntimeException("Department not found"));

        Lecturer lecturer = Lecturer.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .department(department)
                .build();

        lecturer = lecturerRepository.save(lecturer);
        return map(lecturer);
    }

    public List<LecturerDTO.Response> getAllLecturers() {

        return lecturerRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<LecturerDTO.Response> getLecturersByDepartment(String departmentId) {

        return lecturerRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::map)
                .toList();
    }

    public LecturerDTO.Response updateLecturer(String id, LecturerDTO.CreateRequest request) {

        Lecturer lecturer = lecturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lecturer not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        lecturer.setDepartment(department);
        lecturer.getUser().setFullName(request.getFullName());
        lecturer.getUser().setEmail(request.getEmail());
        lecturer.getUser().setPhone(request.getPhone());

        lecturerRepository.save(lecturer);

        return map(lecturer);
    }

    public void deleteLecturer(String id) {

        if (!lecturerRepository.existsById(id)) {
            throw new RuntimeException("Lecturer not found");
        }

        lecturerRepository.deleteById(id);
    }

    private LecturerDTO.Response map(Lecturer lecturer) {

        return new LecturerDTO.Response(
                lecturer.getId(),
                lecturer.getDepartment().getId(),
                lecturer.getDepartment().getName(),
                lecturer.getUser().getFullName(),
                lecturer.getUser().getEmail(),
                lecturer.getUser().getPhone()
        );
    }
}
