package cse41321.containers;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.*;

public class Homework4Test {
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();

    @org.testng.annotations.BeforeMethod
    public void setUp() {
        // Redirect all System.out to consoleContent.
        System.setOut(new PrintStream(this.consoleContent));
    }

    @org.testng.annotations.AfterMethod
    public void tearDown() {
        System.setOut(this.originalStdOut);     // Restore original standard out
        // Clear the consoleContent.
        this.consoleContent = new ByteArrayOutputStream();
    }

    @Test
    public void testStackPushAndPop() {
        Homework4.addLargeNumbers("18274364583929273748525", "8129498165026350236");
        assertTrue(this.consoleContent.toString().contains("52584737292938546347281"));
        assertTrue(this.consoleContent.toString().contains("632053620561894921"));
    }

    @Test
    public void removePunctuation() {
        Homework4.addLargeNumbers("18,274,364,583,929,273,748,525.00", "8,129,498,165,026,350,236");
        assertTrue(this.consoleContent.toString().contains("632053620561894921"));
        assertTrue(this.consoleContent.toString().contains("52584737292938546347281"));
    }
}