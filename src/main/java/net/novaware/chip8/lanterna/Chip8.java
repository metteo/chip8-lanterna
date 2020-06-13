package net.novaware.chip8.lanterna;

import net.novaware.chip8.core.Board;
import net.novaware.chip8.core.clock.ClockGeneratorJvmImpl;
import net.novaware.chip8.core.config.MutableConfig;
import net.novaware.chip8.core.port.DisplayPort;
import net.novaware.chip8.lanterna.device.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        MutableConfig config = new MutableConfig();
        DisplayPort.Mode mode = DisplayPort.Mode.MERGE_FRAME;

        if (title.equals("INVADERS")) {
            mode = DisplayPort.Mode.FALLING_EDGE;
            config.setCpuFrequency(1500);
            config.setLegacyShift(false);
        }

        Board board = newBoardFactory(config, new ClockGeneratorJvmImpl("Lanterna"), new Random()::nextInt).newBoard();

        board.getDisplayPort(DisplayPort.Type.PRIMARY).connect(screen::draw);
        board.getDisplayPort(DisplayPort.Type.PRIMARY).setMode(mode);
        board.getAudioPort().connect(buzzer);
        board.getStoragePort().connect(tape::loadPacket);

        Keyboard k = new Keyboard();
        k.init(board.getKeyPort(), aCase.getTerminal());

        board.powerOn();

        //Uncomment to see when window is changed (resized)
        /*
        Signal.handle(new Signal("WINCH"), sig -> {
            System.out.println("WINCH");
        });
        */
    }
}