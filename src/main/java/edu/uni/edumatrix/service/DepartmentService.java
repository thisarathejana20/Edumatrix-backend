package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.DepartmentDTO;
import edu.uni.edumatrix.model.Department;
import edu.uni.edumatrix.model.Faculty;
import edu.uni.edumatrix.repository.DepartmentRepository;
import edu.uni.edumatrix.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    public DepartmentDTO.Response createDepartment(DepartmentDTO.CreateRequest request) {

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        if (departmentRepository.existsByFacultyIdAndNameIgnoreCase(
                request.getFacultyId(), request.getName()
        )) {
            throw new RuntimeException("Department already exists in this faculty");
        }

        Department department = Department.builder()
                .id(UUID.randomUUID().toString())
                .faculty(faculty)
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();

        departmentRepository.save(department);

        return map(department);
    }

    public List<DepartmentDTO.Response> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public List<DepartmentDTO.Response> getDepartmentsByFaculty(String facultyId) {

        return departmentRepository.findByFacultyId(facultyId)
                .stream()
                .map(this::map)
                .toList();
    }

    public DepartmentDTO.Response updateDepartment(String id, DepartmentDTO.CreateRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        department.setFaculty(faculty);
        department.setName(request.getName());
        department.setCode(request.getCode());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);

        return map(department);
    }

    public void deleteDepartment(String id) {

        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }

        departmentRepository.deleteById(id);
    }

    private DepartmentDTO.Response map(Department department) {

        return new DepartmentDTO.Response(
                department.getId(),
                department.getFaculty().getId(),
                department.getFaculty().getName(),
                department.getName(),
                department.getCode(),
                department.getDescription()
        );
    }
}
