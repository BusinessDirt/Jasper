/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.args;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple command-line argument parser.
 *
 * <p>This parser uses a type-safe approach by registering {@link Arg} instances.
 * It supports arguments with values, as well as valueless flags.
 *
 * <p>Supports arguments in the format:
 *
 * <pre>
 * --key value
 * --flag
 * </pre>
 *
 * Example:
 *
 * <pre>
 * String[] args = { "--server", "localhost", "--port", "8080", "--verbose" };
 * ArgParser argParser = new ArgParser(
 *     new StringArg("server", "default"),
 *     new IntegerArg("port", 8080),
 *     new FlagArg("verbose")
 * );
 * argParser.parse(args);
 * String server = argParser.get("server");
 * int port = argParser.get("port");
 * boolean verbose = argParser.get("verbose");
 * </pre>
 */
public class ArgParser {
    private final Map<String, Arg<?>> argumentMap = new HashMap<>();
    private final Map<String, String> parsedArgs = new HashMap<>();

    /**
     * Constructs a new parser and registers the given arguments.
     * @param args the {@link Arg} instances to register
     */
    public ArgParser(@NotNull Arg<?>... args) {
        for (Arg<?> arg : args) {
            this.argumentMap.put(arg.getKey(), arg);
        }
    }

    /**
     * Parses the command-line arguments from a string array.
     * For each registered argument, its {@link Arg#parse(boolean, String)} method is called.
     * @param args the string array from the main method
     */
    public void parse(@NotNull String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                String key = arg.substring(2);
                String value = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : null;
                this.parsedArgs.put(key, value);
            }
        }

        for (Arg<?> arg : this.argumentMap.values()) {
            String key = arg.getKey();
            arg.parse(this.parsedArgs.containsKey(key), this.parsedArgs.get(key));
        }
    }

    /**
     * Gets the parsed value of a registered argument.
     * @param key the key of the argument
     * @return the parsed value, which can be of any type
     * @param <T> the type of the value
     * @throws IllegalArgumentException if no argument with the given key is registered
     */
    @SuppressWarnings("unchecked")
    public <T> @NotNull T get(@NotNull String key) {
        Arg<T> arg = (Arg<T>) this.argumentMap.get(key);
        if (arg == null) {
            throw new IllegalArgumentException("Argument with key '" + key + "' not registered.");
        }
        return arg.getValue();
    }

    /**
     * Checks if an argument was present on the command line.
     * @param key the key of the argument
     * @return {@code true} if the argument was present, {@code false} otherwise
     */
    public boolean has(@NotNull String key) {
        return this.parsedArgs.containsKey(key);
    }
}
