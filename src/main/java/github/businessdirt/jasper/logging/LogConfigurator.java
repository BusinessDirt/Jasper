package github.businessdirt.jasper.logging;

import github.businessdirt.jasper.logging.builder.PatternBuilder;
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
            configurator.builder.setStatusLevel(level);
            configurator.builder.setConfigurationName("DefaultLogger");
            this.rootLoggerBuilder = configurator.builder.newRootLogger(level);
        }

        public Builder configurationName(String configurationName) {
            configurator.builder.setConfigurationName(configurationName);
            return this;
        }

        public Builder pattern(Consumer<PatternBuilder> action) {
            PatternBuilder patternBuilder = PatternBuilder.builder();
            action.accept(patternBuilder);
            this.pattern = patternBuilder.build();
            return this;
        }

        public void build() {
            Configurator.initialize(configurator.builder.build());
        }
    }
}
