package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.StudentDTO;
import edu.uni.edumatrix.model.Department;
import edu.uni.edumatrix.model.Student;
import edu.uni.edumatrix.model.user.Role;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.DepartmentRepository;
import edu.uni.edumatrix.repository.StudentRepository;
import edu.uni.edumatrix.repository.user.RoleRepository;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.commons.Generator;
import edu.uni.edumatrix.util.constants.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.profiles.active}")
    private String environment;

    public StudentDTO.Response createStudent(
            StudentDTO.CreateRequest request,
            boolean hasSystemAccess
    ) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        Role lecturerRole = roleRepository.findByName(RoleTypes.STUDENT)
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

        Department department = departmentRepository.findById(
                request.getDepartmentId()
        ).orElseThrow(() ->
                new RuntimeException("Department not found"));

        Student student = Student.builder()
                .id(UUID.randomUUID().toString())
                .department(department)
                .registrationNumber(request.getRegistrationNumber())
                .dateOfBirth(request.getDateOfBirth())
                .admissionYear(request.getAdmissionYear())
                .status(StudentStatus.ACTIVE)
                .user(user)
                .build();

        repository.save(student);

        return map(student);
    }

    public List<StudentDTO.Response> getAllStudents() {

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<StudentDTO.Response>
    getStudentsByDepartment(String departmentId) {

        return repository.findByDepartmentId(departmentId)
                .stream()
                .map(this::map)
                .toList();
    }

    public StudentDTO.Response updateStudent(
            String id,
            StudentDTO.CreateRequest request
    ) {

        Student student = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Department department = departmentRepository.findById(
                request.getDepartmentId()
        ).orElseThrow(() ->
                new RuntimeException("Department not found"));

        student.setDepartment(department);
        student.setRegistrationNumber(
                request.getRegistrationNumber());
        student.getUser().setFullName(request.getFullName());
        student.getUser().setEmail(request.getEmail());
        student.getUser().setPhone(request.getPhone());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setAdmissionYear(request.getAdmissionYear());
        student.setStatus(request.getStatus());

        repository.save(student);

        return map(student);
    }

    public void deleteStudent(String id) {

        repository.deleteById(id);
    }

    private StudentDTO.Response map(Student s) {

        return new StudentDTO.Response(
                s.getId(),
                s.getDepartment().getId(),
                s.getDepartment().getName(),
                s.getRegistrationNumber(),
                s.getUser().getFullName(),
                s.getUser().getEmail(),
                s.getUser().getPhone(),
                s.getDateOfBirth(),
                s.getAdmissionYear(),
                s.getStatus()
        );
    }
}
