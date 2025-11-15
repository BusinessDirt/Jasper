package github.businessdirt.jasper.config.dummies;

import github.businessdirt.jasper.config.render.ConfigRenderer;

public class DummyConfigRenderer extends ConfigRenderer<DummyConfig, String> {

    @Override
    public String render(DummyConfig config, String searchQuery) {
        StringBuilder sb = new StringBuilder();

        this.filter(config, searchQuery).forEach((category) ->
                category.items().forEach(property -> {
                    sb.append(category.name());
                    sb.append(".");

                    if (!property.options().subcategory().isBlank()) {
                        sb.append(property.options().subcategory());
                        sb.append(".");
                    }

                    sb.append(property.options().name()).append("\n");
                }));

        return sb.toString();
    }


}
