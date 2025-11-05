package github.businessdirt.jasper.text;

import java.util.List;

@SuppressWarnings("unused")
public interface Text {

    Style getStyle();

    String asString();

    List<Text> getSiblings();

    Text append(Text text);

    default Text copy() {
        return new LiteralText(asString());
    }
}
