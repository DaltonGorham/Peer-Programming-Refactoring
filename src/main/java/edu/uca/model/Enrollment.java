package edu.uca.model;

public record Enrollment(int studentId, int courseId) {
    public Enrollment(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
