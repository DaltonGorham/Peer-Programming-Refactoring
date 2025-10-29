package edu.uca.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainCliTests {

    private InputStream originalSystemIn;
    private PrintStream originalSystemOut;

    @BeforeEach
    void setUp() {
        originalSystemIn = System.in;
        originalSystemOut = System.out;
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    @DisplayName("Test System")
    public void testSystem() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        List<ByteArrayInputStream> inputs = getInputs();

        for (ByteArrayInputStream bais : inputs) {
            System.setIn(bais);
            try {
                MainCli.menuLoop();
            } catch (Exception e) {
                assert false;
            }
        }


        String finalOutput = baos.toString();

        assertTrue(finalOutput.contains("Successfully added student!"), "Output should confirm student addition");
        assertTrue(finalOutput.contains("Successfully added course!"), "Output should confirm course addition");
        assertTrue(finalOutput.contains("Successfully enrolled student!"), "Output should confirm student enrollment");
        assertTrue(finalOutput.contains("B0123456: John Doe (jdoe@cub.uca.edu)"), "Output should list the enrolled student");
        assertTrue(finalOutput.contains("Successfully dropped student!"), "Output should confirm student drop");
        assertTrue(finalOutput.contains("B0123456: John Doe (jdoe@cub.uca.edu)"), "Output should list the created student");
    }

    private List<ByteArrayInputStream> getInputs() {
        List<ByteArrayInputStream> inputs = new ArrayList<>();

        String addStudentInput = "1\nB0123456\nJohn Doe\njdoe@cub.uca.edu\n";
        inputs.add(new ByteArrayInputStream(addStudentInput.getBytes()));
        String addCourseInput = "2\nCS101\nIntroduction to Computer Science\n30\n";
        inputs.add(new ByteArrayInputStream(addCourseInput.getBytes()));
        String enrollInput = "3\nB0123456\nCS101\n";
        inputs.add(new ByteArrayInputStream(enrollInput.getBytes()));
        String listCourseEnrollments = "6\n";
        inputs.add(new ByteArrayInputStream(listCourseEnrollments.getBytes()));
        String dropInput = "4\nB0123456\nCS101\n";
        inputs.add(new ByteArrayInputStream(dropInput.getBytes()));
        String listStudentsInput = "5\n";
        inputs.add(new ByteArrayInputStream(listStudentsInput.getBytes()));

        return inputs;
    }
}
