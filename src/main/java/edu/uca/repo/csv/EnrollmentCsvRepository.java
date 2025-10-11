package edu.uca.repo.csv;

import edu.uca.repo.EnrollmentRepository;
import edu.uca.util.ConfigLoader;
import edu.uca.util.Utilities;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class EnrollmentCsvRepository implements EnrollmentRepository {
    static Map<String, List<String>> enrollments = new LinkedHashMap<>();
    static Map<String, Queue<String>> waitlists = new LinkedHashMap<>();

    @Override
    public void enrollStudent(String studentId, String courseId) {
        enrollments.putIfAbsent(courseId, new java.util.ArrayList<>());
        enrollments.get(courseId).add(studentId);
    }

    @Override
    public void dropStudent(String studentId, String courseId) {
        if (enrollments.containsKey(courseId) && enrollments.get(courseId).contains(studentId)) {
            enrollments.get(courseId).remove(studentId);
            if (waitlists.containsKey(courseId) && !waitlists.get(courseId).isEmpty()) {
                enrollments.get(courseId).add(waitlists.get(courseId).poll());
            }
        } else if (waitlists.containsKey(courseId) && waitlists.get(courseId).contains(studentId)) {
            waitlists.get(courseId).remove(studentId);
        } else {
            throw new RuntimeException("Student not enrolled or waitlisted for course");
        }
    }

    @Override
    public void addToWaitlist(String studentId, String courseId) {
        waitlists.putIfAbsent(courseId, new java.util.LinkedList<>());
        waitlists.get(courseId).add(studentId);
    }

    @Override
    public void removeEnrollmentsByCourse(String courseId) {
        enrollments.remove(courseId);
        waitlists.remove(courseId);
    }

    @Override
    public List<String> getEnrollmentList(String courseId) {
        return enrollments.getOrDefault(courseId, List.of());
    }

    @Override
    public List<String> getWaitlist(String courseId) {
        return waitlists.containsKey(courseId) ? List.copyOf(waitlists.get(courseId)) : List.of();
    }

    @Override
    public int getEnrollmentCount(String courseId) {
        if (enrollments.containsKey(courseId)) {
            return enrollments.get(courseId).size();
        }
        return 0;
    }

    @Override
    public int getWaitlistCount(String courseId) {
        if (waitlists.containsKey(courseId)) {
            return waitlists.get(courseId).size();
        }
        return 0;
    }

    @Override
    public void loadEnrollments() {
        File file = new File(ConfigLoader.getInstance().getProperty("enrollments.csv.file.path"));

        if (!file.exists()) {
            throw new RuntimeException("failed to load enrollment file");
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] values = line.split(",");
                String courseId = values[0];

                enrollments.putIfAbsent(courseId, new java.util.ArrayList<>());

                for (int i = 1; i < values.length; i++) {
                    enrollments.get(courseId).add(values[i]);
                }
            }

            Utilities.audit("LOAD ENROLLMENTS=" + enrollments.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load enrollments", e);
        }
    }

    @Override
    public void saveEnrollments() {
        if (enrollments.isEmpty()) return;

        try (PrintWriter out = new PrintWriter(ConfigLoader.getInstance().getProperty("enrollments.csv.file.path"))) {
            for (String courseId : enrollments.keySet()) {
                StringBuilder line = new StringBuilder(courseId);
                for (String studentId : enrollments.get(courseId)) {
                    line.append(",").append(studentId);
                }
                out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save enrollments", e);
        }
    }
}
