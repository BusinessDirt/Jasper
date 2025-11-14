package github.businessdirt.jasper.config.dummies;

import github.businessdirt.jasper.config.ConfigHandler;
import github.businessdirt.jasper.config.data.Property;
import github.businessdirt.jasper.config.data.PropertyType;

import java.nio.file.Path;

public class DummyConfig extends ConfigHandler {
    @Property(
            type = PropertyType.SWITCH,
            name = "booleanProperty",
            category = "General"
    )
    public boolean booleanProperty = false;

    @Property(
            type = PropertyType.TEXT,
            name = "stringProperty",
            category = "General",
            subcategory = "Sub"
    )
    public String stringProperty = "default";

    @Property(
            type = PropertyType.SLIDER,
            name = "integerProperty",
            category = "Other",
            min = 0,
            max = 100
    )
    public int integerProperty = 50;

    @Property(
            type = PropertyType.SELECTOR,
            name = "selectorProperty",
            category = "Other",
            options = {"A", "B"}
    )
    public String selectorProperty = "A";

    @Property(
            type = PropertyType.TEXT,
            name = "hiddenProperty",
            category = "Hidden",
            hidden = true
    )
    public String hiddenProperty = "secret";


    public DummyConfig(Path configFilePath) {
        super(configFilePath);
    }
}
