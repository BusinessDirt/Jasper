package github.businessdirt.jasper.commands;

import java.io.PrintStream;

public class TestCommandSource implements CommandSource {

    private PrintStream out;

    public TestCommandSource() {}

    public TestCommandSource(PrintStream out) {
        this.out = out;
    }

    @Override
    public PrintStream out() {
        return this.out;
    }
}
