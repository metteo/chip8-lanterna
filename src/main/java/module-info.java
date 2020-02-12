module net.novaware.chip8.lanterna {
    requires net.novaware.chip8.core;

    requires lanterna;
    requires jdk.unsupported; //for signal handling

    requires static java.desktop; //for swing based terminal emulator (optional)

    exports net.novaware.chip8.lanterna;
}