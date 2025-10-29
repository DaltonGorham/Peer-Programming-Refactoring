package edu.uca.service;

import edu.uca.model.Student;
import edu.uca.util.ConfigLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RegistrationServiceTests {

    @BeforeEach
    void setUp() throws IOException {
        Files.createFile(Path.of("students.temp.csv"));
        Files.createFile(Path.of("courses.temp.csv"));
        Files.createFile(Path.of("enrollments.temp.csv"));

        ConfigLoader.getInstance().setProperty("students.csv.file.path", "students.temp.csv");
        ConfigLoader.getInstance().setProperty("courses.csv.file.path", "courses.temp.csv");
        ConfigLoader.getInstance().setProperty("enrollments.csv.file.path", "enrollments.temp.csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Path.of("students.temp.csv"));
        Files.deleteIfExists(Path.of("courses.temp.csv"));
        Files.deleteIfExists(Path.of("enrollments.temp.csv"));
    }

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
