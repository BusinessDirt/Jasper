/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.args;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple command-line argument parser.
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
 * String[] args = { "--server", "localhost", "--port", "8080" };
 * ArgParser argParser = new ArgParser(args);
 * String server = argParser.getString("server", "default");
 * int port = argParser.getInt("port", 8080);
 * </pre>
 */
public class ArgParser {
    private final Map<String, String> argsMap;

    /**
     * Constructs a parser and parses the given arguments.
     *
     * @param args command-line arguments
     */
    public ArgParser(String[] args) {
        this.argsMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                String key = arg.substring(2);
                String value = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : null;
                argsMap.put(key, value);
            }
        }
    }

    /**
     * Gets a string argument by key.
     *
     * @param key argument name (without "--")
     * @param defaultValue value to return if the argument is missing
     * @return the argument value or the default
     */
    public String getString(String key, String defaultValue) {
        return argsMap.getOrDefault(key, defaultValue);
    }

    /**
     * Gets an integer argument by key.
     *
     * @param key argument name (without "--")
     * @param defaultValue value to return if the argument is missing or invalid
     * @return the integer value or the default
     */
    public int getInt(String key, int defaultValue) {
        String value = argsMap.get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Checks if a flag/argument is present.
     *
     * @param key argument name (without "--")
     * @return true if the argument exists
     */
    public boolean has(String key) {
        return argsMap.containsKey(key);
    }
}
