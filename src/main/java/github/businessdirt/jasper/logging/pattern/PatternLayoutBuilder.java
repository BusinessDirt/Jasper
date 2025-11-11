package github.businessdirt.jasper.logging.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PatternLayoutBuilder {
    private final String prefix, suffix;
    private final List<String> content;

    private PatternLayoutBuilder(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.content = new ArrayList<>();
    }

    public static PatternLayoutBuilder builder(String prefix, String suffix) {
        return new PatternLayoutBuilder(prefix, suffix);
    }

    public PatternLayoutBuilder literal(String content) {
        this.content.add(content);
        return this;
    }

    public PatternLayoutBuilder date(String format) {
        return this.literal("%d{" + format + "}");
    }

    public PatternLayoutBuilder date() {
        return this.date("HH:mm:ss");
    }

    public PatternLayoutBuilder level(String formatSpecifier) {
        return this.literal("%" + formatSpecifier + "level");
    }

    public PatternLayoutBuilder level() {
        return this.literal("%level");
    }

    public PatternLayoutBuilder logger(String precisionSpecifier) {
        return this.literal("%c{" + precisionSpecifier + "}");
    }

    public PatternLayoutBuilder logger() {
        return this.logger("1");
    }

    public PatternLayoutBuilder message() {
        return this.literal("%msg");
    }

    public PatternLayoutBuilder threadName() {
        return this.literal("%t");
    }

    public PatternLayoutBuilder threadName(String formatSpecifier) {
        return this.literal("%" + formatSpecifier + "t");
    }

    public PatternLayoutBuilder exception() {
        return this.literal("%ex");
    }

    public PatternLayoutBuilder exception(String options) {
        return this.literal("%ex{" + options + "}");
    }

    public PatternLayoutBuilder rootException() {
        return this.literal("%rEx");
    }

    public PatternLayoutBuilder rootException(String options) {
        return this.literal("%rEx{" + options + "}");
    }

    public PatternLayoutBuilder style(String innerPattern, String styles) {
        return this.literal("%style{" + innerPattern + "}{" + styles + "}");
    }

    public PatternLayoutBuilder highlight(Consumer<PatternLayoutBuilder> innerPattern, String styles) {
        PatternLayoutBuilder innerPatternBuilder = PatternLayoutBuilder.builder("", "");
        innerPattern.accept(innerPatternBuilder);
        return this.literal("%highlight{" + innerPatternBuilder.build() + "}{" + styles + "}");
    }

    public PatternLayoutBuilder highlight(Consumer<PatternLayoutBuilder> innerPattern) {
        PatternLayoutBuilder innerPatternBuilder = PatternLayoutBuilder.builder("", "");
        innerPattern.accept(innerPatternBuilder);
        return this.literal("%highlight{" + innerPatternBuilder.build() + "}");
    }

    public PatternLayoutBuilder processId() {
        return this.literal("%pid");
    }

    public PatternLayoutBuilder hostName() {
        return this.literal("%host");
    }

    public PatternLayoutBuilder mdc() {
        return this.literal("%X");
    }

    public PatternLayoutBuilder mdc(String key) {
        return this.literal("%X{" + key + "}");
    }

    public PatternLayoutBuilder ndc() {
        return this.literal("%x");
    }

    // --- Location-based (PERFORMANCE WARNING) ---

    /**
     * Appends the source file name converter (%F).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternLayoutBuilder fileName() {
        return this.literal("%F");
    }

    /**
     * Appends the line number converter (%L).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternLayoutBuilder lineNumber() {
        return this.literal("%L");
    }

    /**
     * Appends the method name converter (%M).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternLayoutBuilder methodName() {
        return this.literal("%M");
    }

    /**
     * Appends the full location info (%C.%M(%F:%L)).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternLayoutBuilder location() {
        return this.literal("%C.%M(%F:%L)");
    }

    public String build() {
        return this.prefix + String.join("", this.content) + this.suffix;
    }
}
