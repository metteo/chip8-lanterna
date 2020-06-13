@SuppressWarnings("module")
module net.novaware.chip8.lanterna {
    requires net.novaware.chip8.core;

    requires lanterna;
    requires jdk.unsupported; //for signal handling

    requires static java.desktop; //for swing based terminal emulator (optional)

    requires org.apache.logging.log4j;

    exports net.novaware.chip8.lanterna;
}