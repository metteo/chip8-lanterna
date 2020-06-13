package net.novaware.chip8.lanterna.device;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Case {

    private Terminal terminal;

    private TerminalScreen screen;

    public Case() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();

        screen = new TerminalScreen(terminal);
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public TerminalScreen getScreen() {
        return screen;
    }
}
