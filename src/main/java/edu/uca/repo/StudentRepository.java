package edu.uca.repo;

import edu.uca.model.Student;

import java.util.List;

public interface StudentRepository {
    void addStudent(Student student);
    Student getStudentById(String id);
    List<Student> getStudents();

    void loadStudents();
    void saveStudents();
}
