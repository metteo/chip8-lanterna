package net.novaware.chip8.lanterna;

import net.novaware.chip8.core.Board;
import net.novaware.chip8.core.BoardConfig;
import net.novaware.chip8.core.clock.ClockGeneratorJvmImpl;
import net.novaware.chip8.core.port.DisplayPort;
import net.novaware.chip8.lanterna.device.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import static java.lang.System.exit;
import static net.novaware.chip8.core.BoardFactory.newBoardFactory;

public class Chip8 {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            LOG.error("usage: chip8 <pathToRom>");
            exit(1);
        }

        final Path romPath = Path.of(args[0]);
        final String title = romPath.getName(romPath.getNameCount() - 1).toString();

        Tape tape = new Tape(romPath);
        tape.load(); //TODO: temporary preload to verify paths early before lanterna clears the screen

        Case aCase = new Case();

        Screen screen = new Screen(aCase.getScreen());

        Buzzer buzzer = new Buzzer(aCase.getTerminal());
        buzzer.init();

        BoardConfig config = new BoardConfig();
        DisplayPort.Mode mode = DisplayPort.Mode.MERGE_FRAME;

        // TODO: create a ROM library with game profiles instead
        if (title.equals("INVADERS")) {
            mode = DisplayPort.Mode.FALLING_EDGE;
            config.setCpuFrequency(1500);
            config.setLegacyShift(false);
        }

        Board board = newBoardFactory(config, new ClockGeneratorJvmImpl("Lanterna"), new Random()::nextInt).newBoard();

        board.getDisplayPort(DisplayPort.Type.PRIMARY).connect(screen::draw);
        board.getDisplayPort(DisplayPort.Type.PRIMARY).setMode(mode);
        board.getAudioPort().connect(buzzer);
        board.getStoragePort().attachSource(() -> {
            try {
                return tape.load();
            } catch (IOException e) {
                LOG.error("Unable to load tape:" + e);
                return new byte[0];
            }
        });

        Keyboard k = new Keyboard();
        k.init(board.getKeyPort(), aCase.getTerminal());

        board.initialize();
        board.runOnScheduler(Integer.MAX_VALUE);

        /*
        Signal.handle(new Signal("WINCH"), sig -> {
            System.out.println("WINCH");
        });
        */
    }
}