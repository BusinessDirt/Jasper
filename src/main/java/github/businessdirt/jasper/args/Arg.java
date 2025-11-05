package github.businessdirt.jasper.args;

/**
 * An abstract base class for a type-safe command-line argument.
 * @param <T> the type of the argument's value
 */
public abstract class Arg<T> {

    private final String key;
    private final T defaultValue;
    protected T value;

    /**
     * Constructs an argument with a key and a default value.
     * @param key the key of the argument (e.g., "port")
     * @param defaultValue the default value if the argument is not provided or invalid
     */
    public Arg(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    /**
     * @return the key of the argument
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the parsed value of the argument, or the default value if not parsed yet
     */
    public T getValue() {
        return value;
    }

    /**
     * @return the default value of the argument
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Parses the raw string value and updates the argument's value.
     * This method is called by {@link ArgParser}.
     * @param present {@code true} if the argument was present on the command line
     * @param value the raw string value from the command line, or {@code null}
     */
    public abstract void parse(boolean present, String value);
}
