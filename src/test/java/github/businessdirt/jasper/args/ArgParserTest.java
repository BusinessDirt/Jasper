package github.businessdirt.jasper.args;

import github.businessdirt.jasper.args.args.FlagArg;
import github.businessdirt.jasper.args.args.IntegerArg;
import github.businessdirt.jasper.args.args.StringArg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgParserTest {

    private ArgParser argParser;

    @BeforeEach
    void setUp() {
        this.argParser = new ArgParser(
                new StringArg("server", "default"),
                new FlagArg("debug"),
                new StringArg("user", "default"),
                new IntegerArg("port", 8080)
        );
    }

    @Test
    @DisplayName("Should return the correct string value when provided")
    void testGetStringWithValue() {
        this.argParser.parse(new String[] { "--server", "localhost", "--user", "max" });

        assertEquals("localhost", this.argParser.get("server"));
        assertEquals("max", this.argParser.get("user"));
    }

    @Test
    @DisplayName("Should return the default string value when not provided")
    void testGetStringDefault() {
        this.argParser.parse(new String[] {});

        assertEquals("default", this.argParser.get("server"));
    }

    @Test
    @DisplayName("Should return the correct integer value when provided")
    void testGetIntWithValue() {
        this.argParser.parse(new String[] { "--port", "9000" });

        assertEquals(9000, (int) this.argParser.get("port"));
    }

    @Test
    @DisplayName("Should return the default integer value when the provided value is invalid")
    void testGetIntDefaultAndInvalid() {
        //noinspection SpellCheckingInspection
        this.argParser.parse(new String[] { "--port", "notanumber" });

        assertEquals(8080, (int) this.argParser.get("port"));
        assertThrows(IllegalArgumentException.class, () -> this.argParser.get("missing"));
    }

    @DisplayName("Should correctly report the presence of a flag")
    void testHasFlag() {
        this.argParser.parse(new String[] { "--debug" });

        assertTrue(this.argParser.has("debug"));
        assertFalse(this.argParser.has("server"));
    }

    @Test
    @DisplayName("Should parse a mix of different argument types correctly")
    void testMixedArgs() {
        this.argParser.parse(new String[] { "--server", "localhost", "--port", "9000", "--debug" });

        assertEquals("localhost", this.argParser.get("server"));
        assertEquals(9000, (int) this.argParser.get("port"));
        assertTrue(() -> this.argParser.get("debug"));
        assertThrows(IllegalArgumentException.class, () -> this.argParser.get("verbose"));
    }
}