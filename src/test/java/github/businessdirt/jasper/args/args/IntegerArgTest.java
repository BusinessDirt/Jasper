package github.businessdirt.jasper.args.args;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerArgTest {

    @Test
    @DisplayName("IntegerArg should return default value when not present")
    void testIntegerArg_NotPresent() {
        IntegerArg integerArg = new IntegerArg("port", 8080);
        integerArg.parse(false, null);
        assertEquals(8080, integerArg.getValue());
    }

    @Test
    @DisplayName("IntegerArg should return parsed value when present and valid")
    void testIntegerArg_PresentAndValid() {
        IntegerArg integerArg = new IntegerArg("port", 8080);
        integerArg.parse(true, "9090");
        assertEquals(9090, integerArg.getValue());
    }

    @Test
    @DisplayName("IntegerArg should return default value when present but invalid")
    void testIntegerArg_PresentAndInvalid() {
        IntegerArg integerArg = new IntegerArg("port", 8080);
        integerArg.parse(true, "not-a-number");
        assertEquals(8080, integerArg.getValue());
    }

    @Test
    @DisplayName("IntegerArg should return default value when present without a value")
    void testIntegerArg_PresentWithoutValue() {
        IntegerArg integerArg = new IntegerArg("port", 8080);
        integerArg.parse(true, null);
        assertEquals(8080, integerArg.getValue());
    }
}
