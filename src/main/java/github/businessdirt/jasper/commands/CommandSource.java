/* (C) 2025 Maximilian Bollschweiler */
package github.businessdirt.jasper.commands;

import java.io.PrintStream;

/** A marker interface for command sources. */
public interface CommandSource {

    /**
     * @return the output stream the commands should use
     */
    PrintStream out();
}
