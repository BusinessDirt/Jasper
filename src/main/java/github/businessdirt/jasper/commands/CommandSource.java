package github.businessdirt.jasper.commands;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

/** A marker interface for command sources. */
public interface CommandSource {

    /**
     * @return the output stream the commands should use
     */
    @NotNull PrintStream out();
}
