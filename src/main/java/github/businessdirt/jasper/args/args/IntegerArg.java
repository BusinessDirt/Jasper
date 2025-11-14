package github.businessdirt.jasper.args.args;

import github.businessdirt.jasper.args.Arg;
import org.jetbrains.annotations.Nullable;

/**
 * An argument that holds an integer value.
 */
public class IntegerArg extends Arg<Integer> {

    /**
     * Constructs a new integer argument.
     * @param key the key of the argument
     * @param defaultValue the default value
     */
    public IntegerArg(String key, Integer defaultValue) {
        super(key, defaultValue);
    }

    /**
     * Parses the string value into an integer.
     * If the argument is not present, has no value, or the value is not a valid integer,
     * the value is set to the default value.
     * @param present {@code true} if the argument was present on the command line
     * @param value the raw string value, or {@code null}
     */
    @Override
    public void parse(boolean present, @Nullable String value) {
        if (!present || value == null) {
            this.value = getDefaultValue();
            return;
        }

        try {
            this.value = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            this.value = getDefaultValue();
        }
    }
}
