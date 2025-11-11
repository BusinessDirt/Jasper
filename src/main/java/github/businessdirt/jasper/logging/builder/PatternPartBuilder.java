package github.businessdirt.jasper.logging.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PatternPartBuilder {
    private final String prefix, suffix;
    private final List<String> content;

    private PatternPartBuilder(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.content = new ArrayList<>();
    }

    public static PatternPartBuilder builder(String prefix, String suffix) {
        return new PatternPartBuilder(prefix, suffix);
    }

    public static PatternPartBuilder builder() {
        return PatternPartBuilder.builder("", "");
    }

    public static PatternPartBuilder squareBrackets() {
        return PatternPartBuilder.builder("[", "]");
    }

    public static PatternPartBuilder brackets() {
        return PatternPartBuilder.builder("(", ")");
    }

    public PatternPartBuilder literal(String content) {
        this.content.add(content);
        return this;
    }

    public PatternPartBuilder date(String format) {
        return this.literal("%d{" + format + "}");
    }

    public PatternPartBuilder level(String formatSpecifier) {
        return this.literal("%" + formatSpecifier + "level");
    }

    public PatternPartBuilder level() {
        return this.literal("%level");
    }

    public PatternPartBuilder logger() {
        return this.literal("%c");
    }

    public PatternPartBuilder logger(String precisionSpecifier) {
        return this.literal("%c{" + precisionSpecifier + "}");
    }

    public PatternPartBuilder message() {
        return this.literal("%msg");
    }

    public PatternPartBuilder threadName() {
        return this.literal("%t");
    }

    public PatternPartBuilder threadName(String formatSpecifier) {
        return this.literal("%" + formatSpecifier + "t");
    }

    public PatternPartBuilder exception() {
        return this.literal("%ex");
    }

    public PatternPartBuilder exception(String options) {
        return this.literal("%ex{" + options + "}");
    }

    public PatternPartBuilder rootException() {
        return this.literal("%rEx");
    }

    public PatternPartBuilder rootException(String options) {
        return this.literal("%rEx{" + options + "}");
    }

    public PatternPartBuilder style(String innerPattern, String styles) {
        return this.literal("%style{" + innerPattern + "}{" + styles + "}");
    }

    public PatternPartBuilder highlight(Consumer<PatternPartBuilder> innerPattern, String styles) {
        PatternPartBuilder innerPatternBuilder = PatternPartBuilder.builder();
        innerPattern.accept(innerPatternBuilder);
        return this.literal("%highlight{" + innerPatternBuilder.build() + "}{" + styles + "}");
    }

    public PatternPartBuilder highlight(Consumer<PatternPartBuilder> innerPattern) {
        PatternPartBuilder innerPatternBuilder = PatternPartBuilder.builder();
        innerPattern.accept(innerPatternBuilder);
        return this.literal("%highlight{" + innerPatternBuilder.build() + "}");
    }

    public PatternPartBuilder processId() {
        return this.literal("%pid");
    }

    public PatternPartBuilder hostName() {
        return this.literal("%host");
    }

    public PatternPartBuilder mdc() {
        return this.literal("%X");
    }

    public PatternPartBuilder mdc(String key) {
        return this.literal("%X{" + key + "}");
    }

    public PatternPartBuilder ndc() {
        return this.literal("%x");
    }

    // --- Location-based (PERFORMANCE WARNING) ---

    /**
     * Appends the source file name converter (%F).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternPartBuilder fileName() {
        return this.literal("%F");
    }

    /**
     * Appends the line number converter (%L).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternPartBuilder lineNumber() {
        return this.literal("%L");
    }

    /**
     * Appends the method name converter (%M).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternPartBuilder methodName() {
        return this.literal("%M");
    }

    /**
     * Appends the full location info (%C.%M(%F:%L)).
     * <p>
     * <b>PERFORMANCE WARNING:</b> Extremely slow. Avoid in production.
     */
    public PatternPartBuilder location() {
        return this.literal("%C.%M(%F:%L)");
    }

    public String build() {
        return this.prefix + String.join("", this.content) + this.suffix;
    }
}
