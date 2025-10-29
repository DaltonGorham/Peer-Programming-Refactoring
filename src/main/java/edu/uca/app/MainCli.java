package edu.uca.app;

import edu.uca.model.Course;
import edu.uca.service.RegistrationService;

import java.util.Scanner;

import static edu.uca.util.Utilities.*;

//This class is not a main class, it exists for cli implementation which
//needs to be later used for the actual main function in Main.java
public class MainCli {
    private static final RegistrationService regService = new RegistrationService();

    public static void menuLoop() {
        Scanner sc = new Scanner(System.in);

        println("\nMenu:");
        println("1) Add student");
        println("2) Add course");
        println("3) Enroll student in course");
        println("4) Drop student from course");
        println("5) List students");
        println("6) List courses");
        println("0) Exit");
        print("Choose: ");
        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1": addStudentUI(sc); break;
            case "2": addCourseUI(sc); break;
            case "3": enrollUI(sc); break;
            case "4": dropUI(sc); break;
            case "5": listStudents(); break;
            case "6": listCourses(); break;
            case "0": regService.saveData(); System.exit(0);
            default: println("Invalid"); break;
        }
    }

    private static void addStudentUI(Scanner sc) {
        print("Student ID: ");
        String id = sc.nextLine().trim().toUpperCase();
        print("Name: ");
        String name = sc.nextLine().trim();
        print("Email: ");
        String email = sc.nextLine().trim();
        try {
            regService.addStudent(id, name, email);
            println("Successfully added student!");
        } catch (RuntimeException e) {
            println("Failed to add student!");
            println(e.getMessage());
            return;
        }
        audit("ADD_STUDENT=" + id);
    }

    private static void addCourseUI(Scanner sc) {
        print("Course Code: ");
        String code = sc.nextLine().trim().toUpperCase();
        print("Title: ");
        String title = sc.nextLine().trim();
        print("Capacity: ");
        int capacity = Integer.parseInt(sc.nextLine().trim());
        try {
            regService.addCourse(code, title, capacity);
            println("Successfully added course!");
        } catch (RuntimeException e) {
            println("Failed to add course!");
            println(e.getMessage());
            return;
        }
        audit("ADD_COURSE=" + code);
    }

    private static void enrollUI(Scanner sc) {
        print("Student ID: ");
        String studentId = sc.nextLine().trim().toUpperCase();
        print("Course ID: ");
        String courseId = sc.nextLine().trim().toUpperCase();
        try {
            regService.enrollStudent(studentId, courseId);
            println("Successfully enrolled student!");
        } catch (RuntimeException e) {
            println("Failed to enroll student!");
            println(e.getMessage());
            return;
        }
        audit("ENROLL_STUDENT=" + studentId + " COURSE=" + courseId);
    }

    private static void dropUI(Scanner sc) {
        print("Student ID: ");
        String studentId = sc.nextLine().trim().toUpperCase();
        print("Course ID: ");
        String courseId = sc.nextLine().trim().toUpperCase();
        try {
            regService.dropStudent(studentId, courseId);
            println("Successfully dropped student!");
        } catch (RuntimeException e) {
            println("Failed to drop student!");
            println(e.getMessage());
            return;
        }
        audit("DROP_STUDENT=" + studentId + " COURSE=" + courseId);
    }

    private static void listStudents() {
        println("Students:");
        for (var student : regService.getStudents()) {
            println(student.id() + ": " + student.name() + " (" + student.email() + ")");
        }
    }

    private static void listCourses() {
        println("Courses:");
        for (Course c : regService.getCourses())
            println(" - " + c.code() + " " + c.title() + " cap=" + c.capacity()
                    + " enrolled=" + regService.getEnrollmentCount(c.code())
                    + " wait=" + regService.getWaitlist(c.code()));
    }
}
