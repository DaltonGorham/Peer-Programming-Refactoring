package edu.uca.repo.csv;

import edu.uca.model.Course;
import edu.uca.repo.CourseRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseCsvRepository implements CourseRepository {
    static Map<String, Course> courses = new LinkedHashMap<>();

    @Override
    public void addCourse(Course course) {

    }

    @Override
    public Course getCourseById(int courseId) {
        return null;
    }

    @Override
    public List<Course> getCourses() {
        return List.of();
    }

    @Override
    public void loadCourses() {

    }

    @Override
    public void saveCourses() {

    }
}
