package github.businessdirt.jasper.streams;

import github.businessdirt.jasper.streams.charsets.BoxCharset;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * A PrintStream that buffers output and, on each call to flush(),
 * sends the buffered content as a text-based box in "Gemini style" to the
 * actual target OutputStream.
 */
public class BoxPrintStream extends PrintStream {

    private static final String NL = System.lineSeparator();

    private final AutoFlushByteArrayOutputStream internalBuffer;
    private final OutputStream finalDestination;
    private final Charset charset;
    private final BoxCharset boxCharset;

    private boolean isCurrentlyFlushing = false;

    /**
     * Main constructor that sets the final destination, auto-flush behavior, and charset.
     *
     * @param finalDestination The actual target OutputStream (e.g., System.out).
     * @param autoFlush        If true, println will automatically flush.
     * @param charset          The charset to be used.
     */
    public BoxPrintStream(
            @NotNull OutputStream finalDestination,
            boolean autoFlush,
            @NotNull Charset charset,
            @NotNull BoxCharset boxCharset
    ) {
        super(new AutoFlushByteArrayOutputStream(), autoFlush, charset);
        this.internalBuffer = (AutoFlushByteArrayOutputStream) super.out;
        this.finalDestination = finalDestination;
        this.charset = charset;
        this.boxCharset = boxCharset;

        this.internalBuffer.owner = this;
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
    private @NotNull String formatAsBox(@NotNull String content) {
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

        char spacing = this.boxCharset.getChar(false, false, false, false);

        char horizontal = this.boxCharset.getChar(true, true, false, false);
        char vertical = this.boxCharset.getChar(false, false, true, true);

        char topLeftCorner = this.boxCharset.getChar(false, true, false, true);
        char topRightCorner = this.boxCharset.getChar(false, true, true, false);
        char bottomLeftCorner = this.boxCharset.getChar(true, false, false, true);
        char bottomRightCorner = this.boxCharset.getChar(true, false, true, false);

        StringBuilder sb = new StringBuilder();
        String horizontalBar = String.valueOf(horizontal).repeat(maxWidth);

        // Top border
        sb.append(topLeftCorner).append(horizontal).append(horizontalBar)
                .append(horizontal).append(topRightCorner).append(NL);

        // Content lines
        for (String line : lines) {
            sb.append(vertical).append(spacing).append(line);
            // Add padding spaces to make the right edge flush
            if (line.length() < maxWidth) {
                sb.append(String.valueOf(spacing).repeat(maxWidth - line.length()));
            }
            sb.append(spacing).append(vertical).append(NL);
        }

        // Bottom border
        sb.append(bottomLeftCorner).append(horizontal).append(horizontalBar)
                .append(horizontal).append(bottomRightCorner);

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