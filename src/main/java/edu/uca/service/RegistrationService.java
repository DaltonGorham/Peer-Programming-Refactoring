package edu.uca.service;

import edu.uca.model.Course;
import edu.uca.model.Student;
import edu.uca.repo.csv.CourseCsvRepository;
import edu.uca.repo.csv.EnrollmentCsvRepository;
import edu.uca.repo.csv.StudentCsvRepository;

import java.util.List;

public class RegistrationService {
    private static final int MAX_COURSE_CAPACITY = 500;

    CourseCsvRepository courseRepo = new CourseCsvRepository();
    EnrollmentCsvRepository enrollmentRepo = new EnrollmentCsvRepository();
    StudentCsvRepository studentRepo = new StudentCsvRepository();

    public RegistrationService() {
        loadData();
    }

    public void addStudent(String studentId, String name, String email) {
        if (studentId.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new RuntimeException("Fields cannot be empty");
        }

        checkStudentID(studentId);

        if (!email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Email must be valid");
        }

        if (studentRepo.getStudentById(studentId) != null) {
            throw new RuntimeException("Student with id already exists");
        }

        studentRepo.addStudent(new edu.uca.model.Student(studentId, name, email));
    }

    public void removeStudent(String studentId) {
        checkStudentID(studentId);

        if (studentRepo.getStudentById(studentId) == null) {
            throw new RuntimeException("Student with id: " + studentId + " does not exist");
        }

        studentRepo.removeStudent(studentId);
    }

    public void addCourse(String courseId, String title, int capacity) {
        if (courseId.isEmpty() || title.isEmpty()) {
            throw new RuntimeException("Fields cannot be empty");
        }

        if (capacity <= 0 || capacity > MAX_COURSE_CAPACITY) {
            throw new RuntimeException("Capacity must be between 1 and 500");
        }

        checkCourseID(courseId);

        if (courseRepo.getCourseById(courseId) != null) {
            throw new RuntimeException("Course with code: " + courseId + "already exists");
        }

        courseRepo.addCourse(new edu.uca.model.Course(courseId, title, capacity));
    }

    public void enrollStudent(String studentId, String courseId) {
        checkStudentID(studentId);
        checkCourseID(courseId);

        Course course = courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new RuntimeException("Course with id: " + courseId + " does not exist");
        }

        if (studentRepo.getStudentById(studentId) == null) {
            throw new RuntimeException("Student with id: " + studentId + " does not exist");
        }

        int capacity = course.capacity();
        int enrolled = enrollmentRepo.getEnrollmentCount(courseId);

        if (enrolled == capacity) {
            if (enrollmentRepo.getEnrollmentList(courseId).contains(studentId)) {
                throw new RuntimeException("Student with id: " + studentId + " already enrolled");
            }
            if (enrollmentRepo.getWaitlist(courseId).contains(studentId)) {
                throw new RuntimeException("Student with id: " + studentId + " is already on waitlist");
            }
            enrollmentRepo.addToWaitlist(studentId, courseId);
        } else {
            if (enrollmentRepo.getEnrollmentList(courseId).contains(studentId)) {
                throw new RuntimeException("Student with id: " + studentId + " already enrolled");
            }
            enrollmentRepo.enrollStudent(studentId, courseId);
        }
    }

    public void dropStudent(String studentId, String courseId) {
        checkStudentID(studentId);
        checkCourseID(courseId);
        enrollmentRepo.dropStudent(studentId, courseId);
    }


    public List<Student> getStudents() {
        return studentRepo.getStudents();
    }

    public List<Course> getCourses() {
        return courseRepo.getCourses();
    }

    public int getEnrollments(String courseId) {
        checkCourseID(courseId);

        return enrollmentRepo.getEnrollmentCount(courseId);
    }

    public int getWaitlist(String courseId) {
        checkCourseID(courseId);

        return enrollmentRepo.getWaitlistCount(courseId);
    }

    public void saveData() {
        try {
            courseRepo.saveCourses();
            studentRepo.saveStudents();
            enrollmentRepo.saveEnrollments();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadData() {
        try {
            courseRepo.loadCourses();
            studentRepo.loadStudents();
            enrollmentRepo.loadEnrollments();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkCourseID(String id) {
        if (id.isEmpty()) {
            throw new RuntimeException("Course ID cannot be empty");
        }

        if (!id.matches("^[A-Za-z]*\\d+$")) {
            throw new RuntimeException("Course ID must contain alphanumeric characters (e.g., CSCI1440)");
        }

        String numericId = id.replaceAll("[A-Za-z]", "");

        if (Integer.parseInt(numericId) < 0 ) {
            throw new RuntimeException("Course ID must be a positive number");
        }
    }

    private void checkStudentID(String id) {
        if (id.isEmpty()) {
            throw new RuntimeException("Student ID cannot be empty");
        }

        if (!id.matches("^B?\\d+$")) {
            throw new RuntimeException("Student ID must have digits, optionally starting with 'B/b'");
        }

        String numericId = id.startsWith("B") ? id.substring(1) : id;

        if (Integer.parseInt(numericId) < 0) {
            throw new RuntimeException("Student ID must be a positive integer");
        }
    }
}
