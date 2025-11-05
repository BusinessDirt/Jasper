package github.businessdirt.jasper.text;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseText implements Text {

    protected Style style;
    protected final List<Text> siblings;

    protected BaseText() {
        this(Style.EMPTY, new ArrayList<>());
    }

    protected BaseText(Style style, List<Text> siblings) {
        this.style = style;
        this.siblings = siblings;
    }

    @Override
    public Text append(Text text) {
        siblings.add(text);
        return this;
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public List<Text> getSiblings() {
        return siblings;
    }
}
