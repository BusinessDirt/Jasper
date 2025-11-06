package github.businessdirt.jasper.streams;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoxPrintStreamTest {

    private ByteArrayOutputStream testOutput;
    private BoxPrintStream boxedOut;
    private final Charset charset = StandardCharsets.UTF_8;

    private final String NL = System.lineSeparator();

    @BeforeEach
    void setUp() {
        testOutput = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (boxedOut != null) boxedOut.close();
        testOutput.close();
    }

    @Test
    @DisplayName("Test auto-flush creates a box on println")
    void testAutoFlushCreatesBoxOnPrintln() {
        boxedOut = new BoxPrintStream(testOutput, true, charset);
        boxedOut.println("Hello World");

        String expected =
                "┌─────────────┐" + NL +
                "│ Hello World │" + NL +
                "└─────────────┘" + NL;

        assertEquals(expected, testOutput.toString(charset), "The output should be a single boxed line.");
    }

    @Test
    @DisplayName("Test manual-flush combines lines into one box")
    void testManualFlushCombinesLinesIntoOneBox() {
        boxedOut = new BoxPrintStream(testOutput, false, charset);
        boxedOut.println("Line 1");
        boxedOut.print("Line 2 (shorter)"); // .print without line break

        assertEquals(0, testOutput.size(), "Output should be buffered before flush.");

        boxedOut.flush();

        String expected =
                "┌──────────────────┐" + NL +
                "│ Line 1           │" + NL +
                "│ Line 2 (shorter) │" + NL +
                "└──────────────────┘";

        assertEquals(expected, testOutput.toString(charset), "The output should be a multi-line box after flush.");
    }

    @Test
    @DisplayName("Test empty println with auto-flush skips creating a box")
    void testEmptyPrintlnWithAutoFlushSkipsBox() {
        boxedOut = new BoxPrintStream(testOutput, true, charset);

        boxedOut.println("First line");
        boxedOut.println(); // This line should not create a box
        boxedOut.println("Second line");

        String expected =
                "┌────────────┐" + NL +
                "│ First line │" + NL +
                "└────────────┘" + NL +
                NL + // The single line break from println()
                "┌─────────────┐" + NL +
                "│ Second line │" + NL +
                "└─────────────┘" + NL;

        assertEquals(expected, testOutput.toString(charset), "Empty println should not produce a box.");
    }

    @Test
    @DisplayName("Test box correctly pads shorter lines")
    void testBoxCorrectlyPadsShorterLines() {
        boxedOut = new BoxPrintStream(testOutput, true, charset);
        boxedOut.println("This is the longest line\nShort\nAnd another one");

        String horizontalBar = "─".repeat(24);
        String expected =
                "┌─" + horizontalBar + "─┐" + NL +
                "│ This is the longest line │" + NL +
                "│ Short" + " ".repeat(19) + " │" + NL + // 24 - 5 = 19 spaces
                "│ And another one" + " ".repeat(9) + " │" + NL + // 24 - 15 = 9 spaces
                "└─" + horizontalBar + "─┘" + NL;

        assertEquals(expected, testOutput.toString(charset), "Shorter lines should be padded to the width of the longest line.");
    }

    @Test
    @DisplayName("Test close() flushes remaining buffer")
    void testCloseFlushesRemainingBuffer() {
        boxedOut = new BoxPrintStream(testOutput, false, charset);
        boxedOut.print("Last content");

        assertEquals(0, testOutput.size(), "Output should be buffered before close.");

        boxedOut.close(); // 'boxedOut' is set to null in @AfterEach, so not necessary here

        String expected =
                "┌──────────────┐" + NL +
                "│ Last content │" + NL +
                "└──────────────┘";

        assertEquals(expected, testOutput.toString(charset), "Closing the stream should flush the buffer.");
    }
}