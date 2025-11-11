package github.businessdirt.jasper.logging;

import github.businessdirt.jasper.logging.pattern.PatternBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;

import java.util.function.Consumer;

public class LogConfigurator {

    private final ConfigurationBuilder<?> builder;

    private LogConfigurator() {
        this.builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    }

    public static Builder builder(Level level) {
        return new Builder(level);
    }

    public static class Builder {
        private final LogConfigurator configurator = new LogConfigurator();
        private RootLoggerComponentBuilder rootLoggerBuilder;
        private String pattern;

        public Builder(Level level) {
            this.configurator.builder.setStatusLevel(level);
            this.configurator.builder.setConfigurationName("DefaultLogger");
            this.rootLoggerBuilder = this.configurator.builder.newRootLogger(level);
        }

        public Builder configurationName(String configurationName) {
            this.configurator.builder.setConfigurationName(configurationName);
            return this;
        }

        public Builder pattern(Consumer<PatternBuilder> action) {
            PatternBuilder patternBuilder = PatternBuilder.builder();
            action.accept(patternBuilder);
            this.pattern = patternBuilder.build();
            return this;
        }

        public void build() {
            Configurator.initialize(this.configurator.builder.build());
        }
    }
}
