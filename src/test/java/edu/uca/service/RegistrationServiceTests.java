package edu.uca.service;

import edu.uca.model.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RegistrationServiceTests {

    @Test
    @DisplayName("Test User Creation")
    public void testUserCreation() {
        RegistrationService registrationService = new RegistrationService();
        registrationService.addStudent("B0123456", "John Doe", "jdoe@cub.uca.edu");
        List<Student> studentList = registrationService.getStudents();

        assert(studentList.size() == 1);
    }

    @Test
    @DisplayName("Test User Creation and Enrollment")
    public void testUserCreationAndEnrollment() {
        RegistrationService registrationService = new RegistrationService();

        registrationService.addStudent("B0123456", "John Doe", "jdoe@cub.uca.edu");
        registrationService.addStudent("B0654321", "Jane Smith", "jsmith@cub.uca.edu");
        registrationService.addStudent("B0987654", "Alice Johnson", "ajohnson@cub.uca.edu");

        registrationService.addCourse("CPSC101", "Introduction to Computer Science", 100);

        registrationService.enrollStudent("B0123456", "CPSC101");
        registrationService.enrollStudent("B0654321", "CPSC101");
        registrationService.enrollStudent("B0987654", "CPSC101");

        List<String> enrolledStudents = registrationService.getEnrollmentList("CPSC101");
        assert(enrolledStudents.size() == 3);
    }
}
