package github.businessdirt.jasper.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A PrintStream that buffers output and, on each call to flush(),
 * sends the buffered content as a text-based box in "Gemini style" to the
 * actual target OutputStream.
 */
@SuppressWarnings("unused")
public class BoxPrintStream extends PrintStream {

    private final AutoFlushByteArrayOutputStream internalBuffer;
    private final OutputStream finalDestination;
    private final Charset charset;

    private boolean isCurrentlyFlushing = false;

    /**
     * Main constructor that sets the final destination, auto-flush behavior, and charset.
     *
     * @param finalDestination The actual target OutputStream (e.g., System.out).
     * @param autoFlush        If true, println will automatically flush.
     * @param charset          The charset to be used.
     */
    public BoxPrintStream(OutputStream finalDestination, boolean autoFlush, Charset charset) {
        super(new AutoFlushByteArrayOutputStream(), autoFlush, charset);
        this.internalBuffer = (AutoFlushByteArrayOutputStream) super.out;
        this.finalDestination = finalDestination;
        this.charset = charset;

        this.internalBuffer.owner = this;
    }

    /**
     * Convenience constructor with default charset (UTF-8).
     *
     * @param finalDestination The actual target OutputStream.
     * @param autoFlush        If true, println will automatically flush.
     */
    public BoxPrintStream(OutputStream finalDestination, boolean autoFlush) {
        this(finalDestination, autoFlush, StandardCharsets.UTF_8);
    }

    /**
     * Convenience constructor with default charset (UTF-8) and
     * default autoFlush (true).
     *
     * @param finalDestination The actual target OutputStream.
     */
    public BoxPrintStream(OutputStream finalDestination) {
        this(finalDestination, true, StandardCharsets.UTF_8);
    }

    /**
     * Overridden flush() method. This is the core of the logic.
     * Here, the buffer content is retrieved, formatted, and sent to the
     * final destination.
     */
    @Override
    public void flush() {
        if (isCurrentlyFlushing) {
            return;
        }

        this.isCurrentlyFlushing = true;

        try {
            super.flush();

            String content = internalBuffer.toString(charset);
            internalBuffer.reset();

            if (content.trim().isEmpty()) {
                if (!content.isEmpty()) {
                    finalDestination.write(content.getBytes(charset));
                }
                finalDestination.flush();
                return;
            }
            String boxedContent = formatAsBox(content);
            finalDestination.write(boxedContent.getBytes(charset));
            finalDestination.flush();
        } catch (IOException e) {
            // Use the error handling mechanism of PrintStream
            setError();
        } finally {
            isCurrentlyFlushing = false;
        }
    }

    /**
     * Overridden close() method to ensure that all
     * resources are closed correctly.
     */
    @Override
    public void close() {
        try {
            // Flush any remaining content as a final box
            flush();
            // Close the *actual* destination
            finalDestination.close();
        } catch (IOException e) {
            setError();
        } finally {
            // super.close() closes the internalBuffer, which is correct.
            super.close();
        }
    }

    /**
     * Helper method for formatting the content in a box.
     */
    private String formatAsBox(String content) {
        // Remove a single trailing newline (typical from println)
        // to avoid an empty line at the end of the box.
        if (content.endsWith("\r\n")) {
            content = content.substring(0, content.length() - 2);
        } else if (content.endsWith("\n")) {
            content = content.substring(0, content.length() - 1);
        }

        // Split the content by line breaks
        String[] lines = content.split("\\R", -1); // -1 keeps empty lines at the end

        // Find the maximum line width
        int maxWidth = 0;
        for (String line : lines) {
            if (line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }

        StringBuilder sb = new StringBuilder();
        String horizontalBar = "─".repeat(maxWidth);

        // Top border
        sb.append("┌─").append(horizontalBar).append("─┐\n");

        // Content lines
        for (String line : lines) {
            sb.append("│ ").append(line);
            // Add padding spaces to make the right edge flush
            if (line.length() < maxWidth) {
                sb.append(" ".repeat(maxWidth - line.length()));
            }
            sb.append(" │\n");
        }

        // Bottom border
        sb.append("└─").append(horizontalBar).append("─┘");

        return sb.toString();
    }

    private static class AutoFlushByteArrayOutputStream extends ByteArrayOutputStream {
        private BoxPrintStream owner;

        @Override
        public void flush() {
            if (owner != null && !owner.isCurrentlyFlushing)
                owner.flush();
        }
    }
}