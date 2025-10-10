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

        checkId(studentId);

        if (!email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Email must be valid");
        }

        if (studentRepo.getStudentById(studentId) != null) {
            throw new RuntimeException("Student with id already exists");
        }

        studentRepo.addStudent(new edu.uca.model.Student(studentId, name, email));
    }

    public void removeStudent(String studentId) {
        checkId(studentId);

        if (studentRepo.getStudentById(studentId) == null) {
            throw new RuntimeException("Student with id does not exist");
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

        checkId(courseId);

        if (courseRepo.getCourseById(courseId) != null) {
            throw new RuntimeException("Course with code already exists");
        }

        courseRepo.addCourse(new edu.uca.model.Course(courseId, title, capacity));
    }

    public void enrollStudent(String studentId, String courseId) {
        checkId(studentId);
        checkId(courseId);

        int capacity = courseRepo.getCourseById(courseId).capacity();
        int enrolled = enrollmentRepo.getEnrollmentCount(courseId);

        if (enrolled == capacity) {
            if (enrollmentRepo.getWaitlist(courseId).contains(studentId)) {
                throw new RuntimeException("Student already on waitlist");
            }
            enrollmentRepo.addToWaitlist(studentId, courseId);
        } else {
            if (enrollmentRepo.getEnrollmentList(courseId).contains(studentId)) {
                throw new RuntimeException("Student already enrolled in course");
            }
            enrollmentRepo.enrollStudent(studentId, courseId);
        }
    }

    public void dropStudent(String studentId, String courseId) {
        checkId(studentId);
        checkId(courseId);

        try {
            enrollmentRepo.dropStudent(studentId, courseId);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Student> getStudents() {
        return studentRepo.getStudents();
    }

    public List<Course> getCourses() {
        return courseRepo.getCourses();
    }

    public int getEnrollments(String courseId) {
        checkId(courseId);

        return enrollmentRepo.getEnrollmentCount(courseId);
    }

    public int getWaitlist(String courseId) {
        checkId(courseId);

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

    private void checkId(String id) {
        if (id.isEmpty()) {
            throw new RuntimeException("ID cannot be empty");
        }

        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ID must be an integer");
        }

        if (Integer.parseInt(id) < 0) {
            throw new RuntimeException("ID must be a positive integer");
        }
    }
}
