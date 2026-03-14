package edu.uni.edumatrix.service;

import edu.uni.edumatrix.dto.GradeDTO;
import edu.uni.edumatrix.model.Enrollment;
import edu.uni.edumatrix.model.Grade;
import edu.uni.edumatrix.repository.EnrollmentRepository;
import edu.uni.edumatrix.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeDTO.Response publishGrade(
            GradeDTO.CreateRequest request
    ) {

        Enrollment enrollment =
                enrollmentRepository.findById(
                        request.getEnrollmentId()
                ).orElseThrow(() ->
                        new RuntimeException("Enrollment not found"));

        Double marks = request.getMarks();

        String grade;
        Double gradePoint;
        String resultStatus;

        if (marks >= 75) {
            grade = "A";
            gradePoint = 4.0;
            resultStatus = "PASS";
        } else if (marks >= 65) {
            grade = "B";
            gradePoint = 3.0;
            resultStatus = "PASS";
        } else if (marks >= 55) {
            grade = "C";
            gradePoint = 2.0;
            resultStatus = "PASS";
        } else {
            grade = "F";
            gradePoint = 0.0;
            resultStatus = "FAIL";
        }

        Grade g = Grade.builder()
                .id(UUID.randomUUID().toString())
                .enrollment(enrollment)
                .marks(marks)
                .grade(grade)
                .gradePoint(gradePoint)
                .resultStatus(resultStatus)
                .publishedDate(LocalDate.now())
                .build();

        gradeRepository.save(g);

        return map(g);
    }

    public GradeDTO.Response getByEnrollment(String enrollmentId) {

        Grade g = gradeRepository.findByEnrollmentId(
                enrollmentId
        ).orElseThrow(() ->
                new RuntimeException("Grade not found"));

        return map(g);
    }

    private GradeDTO.Response map(Grade g) {

        return new GradeDTO.Response(
                g.getId(),
                g.getEnrollment().getStudent().getUser().getFullName(),
                g.getEnrollment().getCourse().getName(),
                g.getMarks(),
                g.getGrade(),
                g.getGradePoint(),
                g.getResultStatus(),
                g.getPublishedDate()
        );
    }
}
