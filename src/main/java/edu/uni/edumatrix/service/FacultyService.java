package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.FacultyDTO;
import edu.uni.edumatrix.model.Faculty;
import edu.uni.edumatrix.repository.FacultyRepository;
import edu.uni.edumatrix.util.exceptions.custom.ConflictException;
import edu.uni.edumatrix.util.exceptions.http.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepo;

    public FacultyService(FacultyRepository facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    public FacultyDTO.Response create(FacultyDTO.CreateRequest req) {
        if (facultyRepo.existsByNameIgnoreCase(req.getName().trim())) {
            throw new ConflictException("Faculty name already exists: " + req.getName());
        }
        Faculty f = new Faculty();
        f.setName(req.getName().trim());
        f.setCode(req.getCode());
        f.setDescription(req.getDescription());

        f = facultyRepo.save(f);
        return toDto(f);
    }

    public List<FacultyDTO.Response> getAll() {
        return facultyRepo.findAll().stream().map(this::toDto).toList();
    }

    public FacultyDTO.Response get(String id) {
        Faculty f = facultyRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty not found: " + id));
        return toDto(f);
    }

    public FacultyDTO.Response update(String id, FacultyDTO.CreateRequest req) {
        Faculty f = facultyRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty not found: " + id));

        String newName = req.getName().trim();
        if (!f.getName().equalsIgnoreCase(newName) && facultyRepo.existsByNameIgnoreCase(newName)) {
            throw new ConflictException("Faculty name already exists: " + newName);
        }

        f.setName(newName);
        f.setCode(req.getCode());
        f.setDescription(req.getDescription());

        f = facultyRepo.save(f);
        return toDto(f);
    }

    public void delete(String id) {
        if (!facultyRepo.existsById(id)) {
            throw new NotFoundException("Faculty not found: " + id);
        }
        facultyRepo.deleteById(id);
    }

    private FacultyDTO.Response toDto(Faculty f) {
        return new FacultyDTO.Response(f.getId(), f.getName(), f.getCode(), f.getDescription());
    }
}
