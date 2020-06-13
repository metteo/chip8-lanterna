# Chip8 Emulgator TE (Terminal Edition) PoC

![chip8-lanterna](https://github.com/metteo/chip8-lanterna/workflows/chip8-lanterna/badge.svg) [![YouTube Video Likes](https://img.shields.io/youtube/likes/hvN-p5BxH4Y?style=social)](https://www.youtube.com/watch?v=hvN-p5BxH4Y)

![Screenshot of the application](https://repository-images.githubusercontent.com/239994665/0d13a880-ad81-11ea-9760-d381e39cda89)

Chip8 Emulator / Interpreter / Virtual Machine written in Java 11 with Lanterna CLI as front-end

**Warning: Proof of Concept** 

## Prerequisites

* Java Dev Kit 11 installed, with env. variables properly configured:

```
$JAVA_HOME #Linux / Mac

%JAVA_HOME% @REM Windows
```

## Usage

The project is in PoC state so not really usable as a standalone app. 
To run it follow development section below.

## Development

Below instructions will get you a copy of the project up and running on your 
local machine for development and testing purposes.

### Prerequisites

[chip8-core](https://github.com/metteo/chip8-core) installed in local maven repository.

### Building

Compilation
```
./mvnw compile
```

Install to local repository

```
./mvnw install
```

Running from outside IDE (see script header for details):

```
./mvnw exec:exec@run -Drom=<path to ROM>

then after exiting (Esc):

./src/scripts/chip8.sh <path to ROM>
```

## Built With

* [Java 11](https://adoptopenjdk.net/releases.html?variant=openjdk11&jvmVariant=hotspot) - SDK
* [Maven](https://maven.apache.org/) - Dependency Management
* [Chip8 Core](https://github.com/metteo/chip8-core) - Emulator Core
* [Lanterna](https://github.com/mabe02/lanterna) - CLI

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* [r/EmuDev](https://www.reddit.com/r/EmuDev/)
* [Cowgod's Chip-8 Technical Reference v1.0](http://devernay.free.fr/hacks/chip8/C8TECH10.HTM)
* [Mastering Chip-8](http://mattmik.com/files/chip8/mastering/chip8.html)
