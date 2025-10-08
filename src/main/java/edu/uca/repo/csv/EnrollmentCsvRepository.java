package edu.uca.repo.csv;

import edu.uca.model.Enrollment;
import edu.uca.repo.EnrollmentRepository;

import java.util.List;
import java.util.Map;

public class EnrollmentCsvRepository implements EnrollmentRepository {
    static Map<String, Enrollment> enrollments;

    @Override
    public void enrollStudent(int studentId, int courseId) {

    }

    @Override
    public void dropStudent(int studentId, int courseId) {

    }

    @Override
    public List<Enrollment> getEnrollments() {
        return List.of();
    }

    @Override
    public void loadEnrollments() {

    }

    @Override
    public void saveEnrollments() {

    }
}
