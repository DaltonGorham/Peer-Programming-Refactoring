package edu.uca.service;

import edu.uca.model.Course;
import edu.uca.model.Student;
import edu.uca.repo.csv.CourseCsvRepository;
import edu.uca.repo.csv.EnrollmentCsvRepository;
import edu.uca.repo.csv.StudentCsvRepository;

import java.util.List;

public class RegistrationService {
    CourseCsvRepository courseRepo = new CourseCsvRepository();
    EnrollmentCsvRepository enrollmentRepo = new EnrollmentCsvRepository();
    StudentCsvRepository studentRepo = new StudentCsvRepository();

    public RegistrationService() {
        loadData();
    }

    public void addStudent(String id, String name, String email) {
        if (studentRepo.getStudentById(id) != null) {
            throw new RuntimeException("Student with id already exists");
        }

        studentRepo.addStudent(new edu.uca.model.Student(id, name, email));
    }

    public void removeStudent(String id) {
        if (studentRepo.getStudentById(id) == null) {
            throw new RuntimeException("Student with id does not exist");
        }
        studentRepo.removeStudent(id);
    }

    public void removeCourse(String id) {
        courseRepo.removeCourse(id);
        enrollmentRepo.removeEnrollmentsByCourse(id);
    }

    public void addCourse(String code, String title, int capacity) {
        if (courseRepo.getCourseById(code) != null) {
            throw new RuntimeException("Course with code already exists");
        }
        courseRepo.addCourse(new edu.uca.model.Course(code, title, capacity));
    }

    public void enrollStudent(String studentId, String courseId) {
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
        return enrollmentRepo.getEnrollmentCount(courseId);
    }

    public int getWaitlist(String courseId) {
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
}
