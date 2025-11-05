package github.businessdirt.jasper.args.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringArgTest {

    @Test
    @DisplayName("StringArg should return default value when not present")
    void testStringArg_NotPresent() {
        StringArg stringArg = new StringArg("server", "default-server");
        stringArg.parse(false, null);
        assertEquals("default-server", stringArg.getValue());
    }

    @Test
    @DisplayName("StringArg should return parsed value when present")
    void testStringArg_PresentWithValue() {
        StringArg stringArg = new StringArg("server", "default-server");
        stringArg.parse(true, "my-server");
        assertEquals("my-server", stringArg.getValue());
    }

    @Test
    @DisplayName("StringArg should return an empty string when value is empty")
    void testStringArg_PresentWithEmptyValue() {
        StringArg stringArg = new StringArg("server", "default-server");
        stringArg.parse(true, "");
        assertEquals("", stringArg.getValue());
    }

    @Test
    @DisplayName("StringArg should return default value when present without a value")
    void testStringArg_PresentWithoutValue() {
        StringArg stringArg = new StringArg("server", "default-server");
        stringArg.parse(true, null);
        assertEquals("default-server", stringArg.getValue());
    }
}
