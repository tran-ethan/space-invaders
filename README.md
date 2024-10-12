# Space Invaders
This project is a 2D shooter game, inspired by the classic 1978 _Space Invaders_, where players control a spaceship to destroy alien invaders. It is implemented using Open JavaFX 20 with FXML for the graphical interface. The game features multiple levels, each with increasing difficulty, and provides an interactive experience with sound effects, explosions, and various game assets.

## Game Mechanics
- **Spaceship Movement**: The spaceship moves freely in all directions and is controlled using the WASD keys. It initially spawns at the bottom center of the screen.
- **Shooting and Rockets**: Players can fire rockets to eliminate invaders. The type of rocket can be changed by pressing a key. A sound effect is played whenever rockets are fired.
- **Invader Spawning**: The game generates random invaders with different 2D sprites. The first level starts with at least 15 invaders. Their speed and number increase as the player progresses through levels.
- **Game Over Conditions**: The game ends when all aliens are eliminated or when the spaceship is hit by invaders three times. A congratulation screen is displayed when the player wins.

## Getting started
### Prerequisites
Before you can build and run this project, ensure you have the following software installed on your system:
- [Oracle OpenJDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)

### Installation
Make sure Git is installed on your system before continuing with the installation
1. Clone the repository
```shell
git clone https://github.com/tran-ethan/space-invaders.git
```
2. Navigate to the project directory
```shell
cd space-invaders
```
3. Build the project
```shell
./gradlew build
```

## Usage
### Using Gradle Wrapper
Make sure Oracle OpenJDK 18 is properly configured in the `JAVA_HOME` environment variable before trying this method. To run the application using the Gradle Wrapper, execute the following command:
```shell
./gradlew run
```

### Using IDE
You can run the program by directly executing the `main` method in the `MainApp` class directly from within your IDE. Ensure your IDE is configured to use the JDK 18 and has the necessary dependencies in `build.gradle` installed.

## License
This project is licensed under the [MIT License](LICENSE)
