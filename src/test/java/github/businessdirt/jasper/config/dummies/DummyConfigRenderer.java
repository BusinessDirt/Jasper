package github.businessdirt.jasper.config.dummies;

import github.businessdirt.jasper.config.render.ConfigRenderer;

public class DummyConfigRenderer extends ConfigRenderer<DummyConfig, String> {

    private String query;

    public DummyConfigRenderer(DummyConfig config) {
        super(config);

        this.query = "";
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();

        this.filter(this.query).forEach((category, properties) ->
                properties.forEach(property -> {
                    sb.append(category.name());
                    sb.append(".");

                    if (!property.property().subcategory().isBlank()) {
                        sb.append(property.property().subcategory());
                        sb.append(".");
                    }

                    sb.append(property.property().name()).append("\n");
                }));

        return sb.toString();
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
