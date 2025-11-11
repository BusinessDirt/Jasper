package github.businessdirt.jasper.logging.pattern;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PatternBuilder {

    private final StringBuilder patternBuilder;

    private PatternBuilder() {
        this.patternBuilder = new StringBuilder();
    }

    public static PatternBuilder builder() {
        return new PatternBuilder();
    }

    public PatternBuilder literal(String part) {
        this.patternBuilder.append(part);
        return this;
    }

    public PatternBuilder literal(PatternLayoutBuilder part) {
        return this.literal(part.build());
    }

    public PatternBuilder layout(String prefix, String suffix, Consumer<PatternLayoutBuilder> layoutBuilder) {
        PatternLayoutBuilder builder = PatternLayoutBuilder.builder(prefix, suffix);
        layoutBuilder.accept(builder);
        return this.literal(builder.build());
    }

    public PatternBuilder layout(Consumer<PatternLayoutBuilder> layoutBuilder) {
        return this.layout("", "", layoutBuilder);
    }

    public PatternBuilder layoutSquareBrackets(Consumer<PatternLayoutBuilder> layoutBuilder) {
        return this.layout("[", "]", layoutBuilder);
    }

    public PatternBuilder layoutBrackets(Consumer<PatternLayoutBuilder> layoutBuilder) {
        return this.layout("(", ")", layoutBuilder);
    }

    public String build() {
        return this.patternBuilder.append("%n").toString();
    }
}