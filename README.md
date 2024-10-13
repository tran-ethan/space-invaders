# Space Invaders
This project is a 2D shooter game, inspired by the classic 1978 _Space Invaders_, where players control a spaceship to destroy alien invaders. It is implemented using Open JavaFX 20 with FXML for the graphical interface. The game features multiple levels, each with increasing difficulty, and provides an interactive experience with sound effects, explosions, and various game assets.

https://github.com/user-attachments/assets/d409fdd1-36ea-4550-8d9c-645837cf9515

## Game Mechanics
- **Objective**: Eliminate all alien invaders before they reach the bottom of the screen.
- **Spaceship**: The spaceship moves freely in all directions and is controlled using the `WASD` keys. You start with 3 lives and lose a life when you get shot by an invader.
- **Shooting and Rockets**: Players can fire rockets to eliminate invaders by pressing `SPACE`. The type of rocket can be changed by pressing a `R`. The number of rockets you shoot at once increases every level.
- **Invaders**: The game generates invaders at the top of the screen that will slowly make their way to the bottom of the screen while shooting at you. Their speed and number increase as the player progresses through levels.
- **Game Over**: The game ends when all aliens are eliminated on all 3 levels. You lose when an alien reaches the bottom of the screen, when you run out of lives, or when an invader touches your spaceship.

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
