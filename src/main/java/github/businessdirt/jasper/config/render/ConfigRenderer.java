package github.businessdirt.jasper.config.render;

import github.businessdirt.jasper.config.ConfigHandler;

public abstract class ConfigRenderer<T extends ConfigHandler> {

    protected final ConfigHandler config;
    protected String searchQuery;

    public ConfigRenderer(T config) {
        this.config = config;
        this.searchQuery = "";
    }

    public abstract void render();

    public void search(String query) {
        this.searchQuery = query;
    }
}
