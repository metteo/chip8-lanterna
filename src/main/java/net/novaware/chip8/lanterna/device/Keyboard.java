package net.novaware.chip8.lanterna.device;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import net.novaware.chip8.core.port.KeyPort;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static net.novaware.chip8.core.util.UnsignedUtil.ubyte;

/**
 * Key device
 */
public class Keyboard {
    //16 keys

    private volatile byte keyCode;
    private volatile long keyTime;

    private KeyPort keyPort;
    private boolean active = true;

    private ScheduledExecutorService depresser = new ScheduledThreadPoolExecutor(1);
    private ExecutorService poller = Executors.newSingleThreadExecutor(r -> {
        var t = new Thread(r, "KeyPoller");
        t.setDaemon(true);

        return t;
    });

    public void init(KeyPort keyPort, Terminal terminal) {
        this.keyPort = keyPort;

        final Consumer<KeyPort.InputPacket> receiver = this.keyPort.connect(op -> {});

        poller.submit(() -> {
            while (active) {
                try {
                    final KeyStroke keyStroke = terminal.readInput();

                    if (keyStroke.getKeyType() == KeyType.Escape) {
                        System.exit(0);
                    }

                    if (keyStroke.getKeyType() == KeyType.Character) {
                        final byte newKeyCode;

                        switch(keyStroke.getCharacter()) {
                            case '0': newKeyCode = ubyte(0); break;
                            case '1': newKeyCode = ubyte(1); break;
                            case '2': newKeyCode = ubyte(2); break;
                            case '3': newKeyCode = ubyte(3); break;
                            case '4': newKeyCode = ubyte(4); break;
                            case '5': newKeyCode = ubyte(5); break;
                            case '6': newKeyCode = ubyte(6); break;
                            case '7': newKeyCode = ubyte(7); break;
                            case '8': newKeyCode = ubyte(8); break;
                            case '9': newKeyCode = ubyte(9); break;
                            case 'A': newKeyCode = ubyte(0xA); break;
                            case 'B': newKeyCode = ubyte(0xB); break;
                            case 'C': newKeyCode = ubyte(0xC); break;
                            case 'D': newKeyCode = ubyte(0xD); break;
                            case 'E': newKeyCode = ubyte(0xE); break;
                            case 'F': newKeyCode = ubyte(0xF); break;
                            default:
                                newKeyCode = -1;
                        }

                        final boolean wasPressed = keyCode != -1;
                        final boolean isPressed = newKeyCode != -1;
                        final boolean keySwitch = newKeyCode != keyCode;

                        if ((wasPressed && !isPressed) || (wasPressed && keySwitch)) {
                            receiver.accept(new KeyPort.InputPacket() {
                                @Override
                                public KeyPort.Direction getDirection() {
                                    return KeyPort.Direction.UP;
                                }

                                @Override
                                public byte getKeyCode() {
                                    return keyCode;
                                }
                            });
                        }

                        if (isPressed) {
                            receiver.accept(new KeyPort.InputPacket() {
                                @Override
                                public KeyPort.Direction getDirection() {
                                    return KeyPort.Direction.DOWN;
                                }

                                @Override
                                public byte getKeyCode() {
                                    return newKeyCode;
                                }
                            });
                        }

                        keyCode = newKeyCode;
                        keyTime = System.nanoTime();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        depresser.scheduleAtFixedRate(() -> {
            final long now = System.nanoTime();

            if (keyCode != -1 && now - keyTime > 300) {
                receiver.accept(new KeyPort.InputPacket() {
                    @Override
                    public KeyPort.Direction getDirection() {
                        return KeyPort.Direction.UP;
                    }

                    @Override
                    public byte getKeyCode() {
                        return keyCode;
                    }
                });

                keyCode = -1;
                keyTime = System.nanoTime();
            }
        }, 1, 100, TimeUnit.MILLISECONDS);
    }
}
