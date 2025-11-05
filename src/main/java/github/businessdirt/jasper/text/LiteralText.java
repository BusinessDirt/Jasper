package github.businessdirt.jasper.text;

public class LiteralText extends BaseText {

    private final String text;

    public LiteralText(String text) {
        this.text = text;
    }

    @Override
    public String asString() {
        return text;
    }
}
