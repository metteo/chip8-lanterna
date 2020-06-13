package net.novaware.chip8.lanterna.device;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;

import static net.novaware.chip8.core.port.DisplayPort.Packet;

/**
 * Display device
 */
public class Screen {

    private boolean[][] model = new boolean[32][64]; // [y][x]

    private TerminalScreen terminalScreen;

    private TextGraphics tg;

    private int lowerBlockPos = 0;

    public Screen(TerminalScreen terminalScreen) throws IOException {
        this.terminalScreen = terminalScreen;
        terminalScreen.startScreen(); //TODO: stop screen on exit

        tg = terminalScreen.newTextGraphics();
        tg.setBackgroundColor(TextColor.ANSI.DEFAULT);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public void draw(Packet packet) {

        for (int y = 0; y < packet.getRowCount(); ++y) {
            for (int x = 0; x < packet.getColumnCount(); ++x) {
                model[y][x] = packet.getPixel(x, y);
            }
        }

        refresh();
    }

    public void refresh() {
        TerminalSize terminalSize = terminalScreen.getTerminalSize();
        final TerminalSize terminalReSize = terminalScreen.doResizeIfNecessary();
        terminalSize = terminalReSize == null ? terminalSize : terminalReSize;

        for (int y = 0; y < Math.min(32 - 1, terminalSize.getRows() * 2); y+=2) {
            for (int x = 0; x < Math.min(64, terminalSize.getColumns()); ++x) {
                boolean upperPixel = model[y][x];
                boolean lowerPixel = model[y+1][x];

                String s = "░";
                if (upperPixel && lowerPixel) {
                    s = "█";
                } else if (upperPixel) {
                    s = "▀";
                } else if (lowerPixel) {
                    s = "▄";
                } else {
                    s = " ";
                }

                tg.putString(x, y/2, s);
            }
        }

        //visualize redraw
        tg.putString(lowerBlockPos, 16, "░");
        lowerBlockPos = (lowerBlockPos + 1) % 64;
        tg.putString(lowerBlockPos, 16, "█");
        try {
            terminalScreen.refresh();
        } catch (IOException e) {
            e.printStackTrace(); //TODO: handle exception
        }
    }
}
