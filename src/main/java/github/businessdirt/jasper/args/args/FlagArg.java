package github.businessdirt.jasper.args.args;

import github.businessdirt.jasper.args.Arg;

/**
 * An argument that represents a boolean flag.
 * If the flag is present, the value is true. If a value is provided (e.g., --flag false), it is parsed as a boolean.
 * If the flag is not present, the value is false.
 */
public class FlagArg extends Arg<Boolean> {

    /**
     * Constructs a new flag argument.
     * The default value is always {@code false}.
     * @param key the key for the flag
     */
    public FlagArg(String key) {
        super(key, false);
    }

    /**
     * Parses the presence and value of the flag.
     * If present without a value, it's {@code true}.
     * If present with a value, the value is parsed as a boolean.
     * If not present, it's {@code false}.
     * @param present {@code true} if the argument was present on the command line
     * @param value the raw string value, or {@code null}
     */
    @Override
    public void parse(boolean present, String value) {
        if (!present) {
            this.value = false;
            return;
        }

        if (value == null) {
            this.value = true;
        } else {
            this.value = Boolean.parseBoolean(value);
        }
    }
}
