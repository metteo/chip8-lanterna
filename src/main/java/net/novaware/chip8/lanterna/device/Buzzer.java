package net.novaware.chip8.lanterna.device;

import com.googlecode.lanterna.terminal.Terminal;
import net.novaware.chip8.core.port.AudioPort;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Sound device
 */
public class Buzzer implements Consumer<AudioPort.Packet> {

    private final Terminal terminal;

    public Buzzer(Terminal terminal) {
        this.terminal = terminal;
    }

    public void init() {

    }

    public void startBuzzing() {
        try {
            terminal.bell();
        } catch (IOException e) {
            e.printStackTrace(); //TODO: handle exception
        }
    }

    public void stopBuzzing() {
        //TODO: NOOP?, no way to keep using the bell, unless we use some thread loop
    }

    @Override
    public void accept(AudioPort.Packet buzz) {
        if (buzz.isSoundOn()) {
            startBuzzing();
        } else {
            stopBuzzing();
        }
    }
}


