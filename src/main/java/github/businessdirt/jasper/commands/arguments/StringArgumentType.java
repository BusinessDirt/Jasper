package github.businessdirt.jasper.commands.arguments;

import github.businessdirt.jasper.commands.StringReader;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a string argument type. This argument type will consume a single word and return it as a string.
 */
public class StringArgumentType implements ArgumentType<String> {

    /**
     * Parses a string from the reader.
     *
     * @param reader the string reader to parse
     * @return the parsed string
     */
    @Override
    public @NotNull String parse(@NotNull StringReader reader) {
        return reader.readString();
    }
}
