package edu.uca.repo.csv;

import edu.uca.model.Student;
import edu.uca.repo.StudentRepository;
import edu.uca.util.ConfigLoader;
import edu.uca.util.Utilities;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentCsvRepository implements StudentRepository {
    Map<String, Student> students = new LinkedHashMap<>();

    @Override
    public void addStudent(Student student) {
        students.put(student.id(), student);
    }

    @Override
    public void removeStudent(String id) { students.remove(id); }

    @Override
    public Student getStudentById(String id) {
        return students.get(id);
    }

    @Override
    public List<Student> getStudents() {
        return students.values().stream().toList();
    }

    @Override
    public void loadStudents() {
        File file = new File(ConfigLoader.getInstance().getProperty("students.csv.file.path"));

        if (!file.exists()) {
            throw new RuntimeException("failed to load student file");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Student student = new Student(values[0], values[1], values[2]);
                students.put(student.id(), student);
            }

            Utilities.audit("LOAD students=" + students.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load students", e);
        }
    }

    @Override
    public void saveStudents() {
        if (students.isEmpty()) return;

        try (PrintWriter out = new PrintWriter(ConfigLoader.getInstance().getProperty("students.csv.file.path"))) {
            for (Student student : students.values()) {
                out.println(student.id() + "," + student.name() + "," + student.email());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save students", e);
        }
    }
}
