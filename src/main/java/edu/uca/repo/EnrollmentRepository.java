package edu.uca.repo;

import edu.uca.model.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    void enrollStudent(int studentId, int courseId);
    void dropStudent(int studentId, int courseId);
    List<Enrollment> getEnrollments();

    void loadEnrollments();
    void saveEnrollments();
}
