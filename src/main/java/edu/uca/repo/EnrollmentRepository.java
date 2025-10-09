package edu.uca.repo;

import java.util.List;

public interface EnrollmentRepository {
    void enrollStudent(String studentId, String courseId);
    void dropStudent(String studentId, String courseId);
    void addToWaitlist(String studentId, String courseId);
    void removeEnrollmentsByCourse(String courseId);

    List<String> getEnrollmentList(String courseId);
    List<String> getWaitlist(String courseId);

    int getWaitlistCount(String courseId);
    int getEnrollmentCount(String courseId);

    void loadEnrollments();
    void saveEnrollments();
}
