package github.businessdirt.jasper.logging.builder;

@SuppressWarnings("unused")
public class PatternBuilder {

    private final StringBuilder patternBuilder;

    private PatternBuilder() {
        this.patternBuilder = new StringBuilder();
    }

    public static PatternBuilder builder() {
        return new PatternBuilder();
    }

    public PatternBuilder append(String part) {
        this.patternBuilder.append(part);
        return this;
    }

    public PatternBuilder append(PatternPartBuilder part) {
        return this.append(part.build());
    }

    public PatternBuilder appendSpace() {
        return this.append(" ");
    }

    public String build() {
        return this.patternBuilder.append("%n").toString();
    }
}