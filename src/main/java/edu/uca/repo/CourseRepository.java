package edu.uca.repo;

import edu.uca.model.Course;

import java.util.List;

public interface CourseRepository {
    void addCourse(Course course);
    Course getCourseById(String courseId);
    List<Course> getCourses();

    void loadCourses();
    void saveCourses();
}
