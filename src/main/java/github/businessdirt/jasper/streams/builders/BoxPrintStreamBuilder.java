package github.businessdirt.jasper.streams.builders;

import github.businessdirt.jasper.streams.BoxPrintStream;
import github.businessdirt.jasper.streams.charsets.BoxCharset;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public class BoxPrintStreamBuilder {

    private final OutputStream finalDestination;
    private boolean autoFlush;
    private Charset charset;
    private BoxCharset boxCharset;

    public BoxPrintStreamBuilder(@NotNull OutputStream finalDestination) {
        this.finalDestination = finalDestination;
        this.autoFlush = false;
        this.charset = StandardCharsets.UTF_8;
        this.boxCharset = BoxCharset.ROUNDED;
    }

    public @NotNull BoxPrintStream build() {
        return new BoxPrintStream(this.finalDestination, this.autoFlush, this.charset, this.boxCharset);
    }

    public @NotNull BoxPrintStreamBuilder autoFlush() {
        this.autoFlush = true;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder charset(@NotNull Charset charset) {
        this.charset = charset;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder utf8() {
        this.charset = StandardCharsets.UTF_8;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder utf16() {
        this.charset = StandardCharsets.UTF_16;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder utf32() {
        this.charset = StandardCharsets.UTF_32;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder ascii() {
        this.charset = StandardCharsets.US_ASCII;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder boxCharset(BoxCharset boxCharset) {
        this.boxCharset = boxCharset;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder singleBox() {
        this.boxCharset = BoxCharset.SINGLE;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder doubleBox() {
        this.boxCharset = BoxCharset.DOUBLE;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder roundedBox() {
        this.boxCharset = BoxCharset.ROUNDED;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder asciiBox() {
        this.boxCharset = BoxCharset.ASCII;
        return this;
    }

    public @NotNull BoxPrintStreamBuilder heavyBox() {
        this.boxCharset = BoxCharset.HEAVY;
        return this;
    }
}
