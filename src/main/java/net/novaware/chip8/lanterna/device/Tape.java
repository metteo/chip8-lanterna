package net.novaware.chip8.lanterna.device;

import net.novaware.chip8.core.port.StoragePort;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.novaware.chip8.core.util.UnsignedUtil.uint;

/**
 * Storage device
 */
public class Tape {
    private Path romPath;

    public Tape(Path romPath) {
        this.romPath = romPath;
    }

    public byte[] load() throws IOException {
        final InputStream binary = Files.newInputStream(romPath);

        final byte[] bytes;
        try (binary) {
            bytes = binary.readAllBytes(); //TODO handle exception
        }

        return bytes;
    }

    public StoragePort.Packet loadPacket() {
        try {
            byte[] bytes = load();
            return new StoragePort.Packet() {
                @Override
                public int getSize() {
                    return bytes.length;
                }

                @Override
                public byte getByte(short address) {
                    return bytes[uint(address)];
                }
            };
        } catch (IOException e) {
            throw new RuntimeException("Unable to load: ", e);
        }
    }
}
