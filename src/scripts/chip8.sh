#!/bin/bash

# execute "./mvnw exec:exec@run" first and press Esc when the game loads (running through maven is glitchy)
# above execution creates a file under ./target/modulepath which is used to load modules

DEBUG=-agentlib:jdwp=transport=dt_socket,address=127.0.0.1:5005,suspend=y,server=y

java @target/modulepath \
  -ea \
  -Dfile.encoding=UTF-8 \
  -Djava.awt.headless=true \
  $DEBUG \
  --module net.novaware.chip8.lanterna/net.novaware.chip8.lanterna.Chip8 "$@"
