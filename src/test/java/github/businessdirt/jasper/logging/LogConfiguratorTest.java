package github.businessdirt.jasper.logging;

import github.businessdirt.jasper.logging.builder.PatternPartBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class LogConfiguratorTest {

    @Test
    void test() {
        LogConfigurator.builder(Level.INFO)
                .pattern(patternBuilder -> patternBuilder
                        .append(PatternPartBuilder.squareBrackets().date("HH:mm:ss"))
                        .appendSpace()
                        .append(PatternPartBuilder.squareBrackets().threadName().literal("/").level())
                        .appendSpace()
                        .append(PatternPartBuilder.brackets().logger("1"))
                        .append(":").appendSpace()
                        .append(PatternPartBuilder.builder().highlight(PatternPartBuilder::message,
                                "{FATAL=red bright, ERROR=red, WARN=yellow, INFO=green, DEBUG=blue, TRACE=white}"))
                ).build();

        Logger logger = LogManager.getLogger(LogConfiguratorTest.class);
        logger.info("This is an info message");
        logger.warn("This is a warning message");
        logger.error("This is an error message");
    }
}
