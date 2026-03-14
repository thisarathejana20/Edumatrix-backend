package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.HostelDTO;
import edu.uni.edumatrix.dto.LecturerDTO;
import edu.uni.edumatrix.model.Department;
import edu.uni.edumatrix.model.Faculty;
import edu.uni.edumatrix.model.Hostel;
import edu.uni.edumatrix.model.Lecturer;
import edu.uni.edumatrix.model.user.Role;
import edu.uni.edumatrix.model.user.User;
import edu.uni.edumatrix.repository.DepartmentRepository;
import edu.uni.edumatrix.repository.HostelRepository;
import edu.uni.edumatrix.repository.LecturerRepository;
import edu.uni.edumatrix.repository.user.RoleRepository;
import edu.uni.edumatrix.repository.user.UserRepository;
import edu.uni.edumatrix.util.commons.Generator;
import edu.uni.edumatrix.util.constants.EnvironmentTypes;
import edu.uni.edumatrix.util.constants.ExtraConstants;
import edu.uni.edumatrix.util.constants.RoleTypes;
import edu.uni.edumatrix.util.constants.UserStatus;
import edu.uni.edumatrix.util.exceptions.custom.ConflictException;
import edu.uni.edumatrix.util.exceptions.http.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HostelService {

    private final HostelRepository hostelRepository;

    @Transactional
    public HostelDTO.Response createLecturer(
            HostelDTO.CreateRequest request
    ) {
        if (hostelRepository.existsByNameIgnoreCase((request.getName().trim()))) {
            throw new ConflictException("Hostel name already exists: " + request.getName());
        }
        Hostel hostel = new Hostel();
        hostel.setName(request.getName().trim());
        hostel.setLocation(request.getLocation());
        hostel.setCapacity(request.getCapacity());
        hostel.setCreatedAt(Instant.now());
        hostel = hostelRepository.save(hostel);
        return map(hostel);
    }

    public List<HostelDTO.Response> getAllHostels() {

        return hostelRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    public HostelDTO.Response updateHostel(String id, HostelDTO.CreateRequest request) {

        Hostel hostel = hostelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hostel not found"));

        hostel.setName(request.getName().trim());
        hostel.setLocation(request.getLocation());
        hostel.setCapacity(request.getCapacity());
        hostel.setCreatedAt(Instant.now());
        hostel = hostelRepository.save(hostel);
        return map(hostel);
    }

    public void deleteHostel(String id) {

        if (!hostelRepository.existsById(id)) {
            throw new NotFoundException("Lecturer not found");
        }
        hostelRepository.deleteById(id);
    }

    private HostelDTO.Response map(Hostel hostel) {

        return new HostelDTO.Response(
                hostel.getId(),
                hostel.getName(),
                hostel.getLocation(),
                hostel.getCapacity()
        );
    }
}
