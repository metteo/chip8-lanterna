package net.novaware.chip8.lanterna;

import net.novaware.chip8.core.Board;
import net.novaware.chip8.lanterna.device.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

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
        Tape tape = new Tape(romPath);
        tape.load(); //TODO: temporary preload to verify paths early before lanterna clears the screen

        Case aCase = new Case();

        Screen screen = new Screen(aCase.getScreen());

        Buzzer buzzer = new Buzzer(aCase.getTerminal());
        buzzer.init();

        Board board = newBoardFactory().newBoard();
        board.init();

        board.getDisplayPort().attach(screen::draw);
        board.getAudioPort().attach(buzzer);
        board.getStoragePort().load(tape.load());

        Keyboard k = new Keyboard();
        k.init(board.getKeyPort(), aCase.getTerminal());

        board.run(Integer.MAX_VALUE);

        /*
        Signal.handle(new Signal("WINCH"), sig -> {
            System.out.println("WINCH");
        });
        */
    }
}