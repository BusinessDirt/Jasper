package github.businessdirt.jasper.args.args;

import github.businessdirt.jasper.args.Arg;

/**
 * An argument that holds a string value.
 */
public class StringArg extends Arg<String> {

    /**
     * Constructs a new string argument.
     * @param key the key of the argument
     * @param defaultValue the default value
     */
    public StringArg(String key, String defaultValue) {
        super(key, defaultValue);
    }

    /**
     * Parses the string value.
     * If the argument is not present or has no value, the value is set to the default value.
     * @param present {@code true} if the argument was present on the command line
     * @param value the raw string value, or {@code null}
     */
    @Override
    public void parse(boolean present, String value) {
        if (!present || value == null) {
            this.value = getDefaultValue();
            return;
        }

        this.value = value;
    }
}
