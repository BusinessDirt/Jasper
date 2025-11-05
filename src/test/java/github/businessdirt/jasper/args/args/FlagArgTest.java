package github.businessdirt.jasper.args.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlagArgTest {

    @Test
    @DisplayName("Flag should be false when not present")
    void testFlagArg_NotPresent() {
        FlagArg flagArg = new FlagArg("debug");
        flagArg.parse(false, null);
        assertFalse(flagArg.getValue());
    }

    @Test
    @DisplayName("Flag should be true when present without a value")
    void testFlagArg_PresentWithoutValue() {
        FlagArg flagArg = new FlagArg("debug");
        flagArg.parse(true, null);
        assertTrue(flagArg.getValue());
    }

    @Test
    @DisplayName("Flag should be true when present with value 'true'")
    void testFlagArg_PresentWithTrue() {
        FlagArg flagArg = new FlagArg("debug");
        flagArg.parse(true, "true");
        assertTrue(flagArg.getValue());
    }

    @Test
    @DisplayName("Flag should be false when present with value 'false'")
    void testFlagArg_PresentWithFalse() {
        FlagArg flagArg = new FlagArg("debug");
        flagArg.parse(true, "false");
        assertFalse(flagArg.getValue());
    }

    @Test
    @DisplayName("Flag should be false when present with a non-boolean value")
    void testFlagArg_PresentWithNonBoolean() {
        FlagArg flagArg = new FlagArg("debug");
        flagArg.parse(true, "not-a-boolean");
        assertFalse(flagArg.getValue());
    }
}