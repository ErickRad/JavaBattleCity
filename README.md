#Java Battle City
Érick Radmann, Eduardo Timm Buss, DIogo Pereira Ribeiro
 
## Introduction
This is a modified version of the video game Battle City. Battle City is a really popular and classic video game that was created in 90s. The player needs to destroy different enemy tanks, while trying to keep them away from his or her own base. All the sprites we get from https://www.spriters-resource.com/nes/batcity/asset/60016/ spritesheets.


## Requirements 
Requires Java JDK 1.8.
Requires a Java JDK Headful version due to the java.awt and java.swing libraries.
 
## Installation
### 1. Clone the repository:

```bash
git clone https://github.com/ErickRad/JavaBattleCity
```

### 2. Change into the project directory (the repo folder is named `JavaBattleCity`):

```bash
cd Battle-City
```

### 3. Make sure you have a Java JDK (headful) installed and available on `PATH`:

```bash
java -version
# should show a JDK (e.g. "openjdk version \"1.8.0_xxx\"" or newer)
```

### 4. Compile all Java sources into an `out` directory (run this from the project root):

```bash
mkdir -p out
javac -Xlint -d out $(find src -name "*.java")
```

### 5. Run the game (the main class is `GameMain.GameMain`):

```bash
java -cp out GameMain.GameMain
```

### 6. Notes and troubleshooting
- Run the commands from the project root (the folder that contains `src/`).
- This game uses AWT/Swing so it requires a headful JDK and an X11/Wayland display (on Linux). If running on a headless server, use SSH with X forwarding (`ssh -X`) or run locally.
- If images or sounds are not found, ensure the `images/` and `sounds/` folders are present next to `src/`.
- To rebuild quickly after small changes, you can re-run the `javac` command above; it recompiles only Java files.

Optional: create a runnable JAR (not required to play):

```bash
cd out
echo "Main-Class: GameMain.GameMain" > manifest.txt
jar cfm ../BattleCity.jar manifest.txt $(find . -name "*.class")
cd ..
java -jar BattleCity.jar
```

