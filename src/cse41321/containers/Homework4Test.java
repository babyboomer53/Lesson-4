package cse41321.containers;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.*;

public class Homework4Test {
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();
    String theAnswer = "18282494082094300098761";

    @BeforeMethod
    public void setUp() {
        // Redirect all System.out to consoleContent.
        System.setOut(new PrintStream(this.consoleContent));
    }

    @AfterMethod
    public void tearDown() {
        System.setOut(this.originalStdOut);     // Restore original standard out
        // Clear the consoleContent.
        this.consoleContent = new ByteArrayOutputStream();
    }

    @Test
    public void withReallyLongNumbers() {
        // By the way, I couldn't find anything on the Internet that could add these two numbers!
        Homework4.addLargeNumbers("18274364583929273748525", "8129498165026350236");
        assertTrue(this.consoleContent.toString().contains(theAnswer));
    }

    @Test
    public void numbersContainingSpaces() {
        Homework4.addLargeNumbers("18 274 364 583 929 273 748 525", "8 129 498 165 026 350 236");
        assertTrue(this.consoleContent.toString().contains(theAnswer));
    }

    @Test
    public void withAnEmptyString() {
        Homework4.addLargeNumbers("18274364583929273748525", "");
        assertTrue(this.consoleContent.toString().contains("18274364583929273748525"));
    }

    @Test
    public void theOtherStringIsMissing() {
        Homework4.addLargeNumbers("", "8129498165026350236");
        assertTrue(this.consoleContent.toString().contains("8129498165026350236"));
    }


    @Test
    public void bothArgumentsAreEmpty() {
        Homework4.addLargeNumbers("", "");
        assertTrue(this.consoleContent.toString().contains(""));
    }

    @Test
    public void theSameArgumentsInReverseOrder() {
        Homework4.addLargeNumbers("8129498165026350236", "18274364583929273748525");
        assertTrue(this.consoleContent.toString().contains(theAnswer));
    }

    @Test
    public void removePunctuationAndFractionalComponent() {
        Homework4.addLargeNumbers("18,274,364,583,929,273,748,525.1234", "8,129,498,165,026,350,236.5678");
        assertTrue(this.consoleContent.toString().contains(theAnswer));
    }

    @Test
    public void morePunctuation() {
        Homework4.addLargeNumbers("18_274_364_583_929_273_748_525.1234", "8_129_498_165_026_350_236.5678");
        assertTrue(this.consoleContent.toString().contains(theAnswer));
    }

    @Test
    public void preliminaryExam() {
        Homework4.addLargeNumbers("592.25", "3,784.50");
        assertTrue(this.consoleContent.toString().contains("4376"));
    }
}