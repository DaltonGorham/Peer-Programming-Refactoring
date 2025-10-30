package edu.uca.repo.csv;

import edu.uca.model.Course;
import edu.uca.repo.CourseRepository;
import edu.uca.util.ConfigLoader;
import edu.uca.util.Utilities;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseCsvRepository implements CourseRepository {
    static Map<String, Course> courses = new LinkedHashMap<>();

    @Override
    public void addCourse(Course course) {
        courses.put(course.code(), course);
    }

    @Override
    public void removeCourse(String courseId) { courses.remove(courseId); }

    @Override
    public Course getCourseById(String courseId) {
        return courses.get(courseId);
    }

    @Override
    public List<Course> getCourses() { return courses.values().stream().toList(); }

    @Override
    public void loadCourses() {
        File file = new File(ConfigLoader.getInstance().getProperty("courses.csv.file.path"));

        boolean createIfMissing = Boolean.parseBoolean(
                ConfigLoader.getInstance().getProperty("csv.create-if-missing")
        );
        if (!file.exists()) {
            if (createIfMissing) {
                try {
                    if (file.createNewFile()) {
                        Utilities.audit("Created new course file at " + file.getAbsolutePath());
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException("failed to create course file", e);
                }
            } else {
                throw new RuntimeException("Failed to load course file");
            }
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] values = line.split(",");
                Course course = new Course(values[0], values[1], Integer.parseInt(values[2]));
                courses.put(course.code(), course);
            }
            Utilities.audit("LOAD COURSES=" + courses.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load courses", e);
        }
    }

    @Override
    public void saveCourses() {
        if (courses.isEmpty()) return;

        try (PrintWriter out = new PrintWriter(ConfigLoader.getInstance().getProperty("courses.csv.file.path"))) {
            for  (Course course : courses.values()) {
                out.println(course.code() + "," + course.title() + "," + course.capacity());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save courses", e);
        }
    }
}
