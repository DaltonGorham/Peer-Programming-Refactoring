package edu.uca.repo.csv;

import edu.uca.model.Student;
import edu.uca.repo.StudentRepository;

import java.util.List;

public class StudentCsvRepository implements StudentRepository {
    @Override
    public void addStudent(Student student) {

    }

    @Override
    public Student getStudentById(int id) {
        return null;
    }

    @Override
    public List<Student> getStudents() {
        return List.of();
    }

    @Override
    public void loadStudents() {

    }

    @Override
    public void saveStudents() {

    }
}
