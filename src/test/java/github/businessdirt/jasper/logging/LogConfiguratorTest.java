package github.businessdirt.jasper.logging;

import github.businessdirt.jasper.logging.pattern.PatternLayoutBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class LogConfiguratorTest {

    @Test
    void test() {
        LogConfigurator.builder(Level.INFO)
                .pattern(pattern -> pattern
                        .layoutSquareBrackets(PatternLayoutBuilder::date).literal(" ")
                        .layoutSquareBrackets(layout -> layout.threadName().literal("/").level()).literal(" ")
                        .layoutBrackets(PatternLayoutBuilder::logger).literal(": ")
                        .layout(layout -> layout.highlight(PatternLayoutBuilder::message,
                                "{FATAL=red bright, ERROR=red, WARN=yellow, INFO=green, DEBUG=blue, TRACE=white}"))
                ).build();

        Logger logger = LogManager.getLogger(LogConfiguratorTest.class);
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
    }
}
