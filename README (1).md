# Return to Moria — 2D Platformer (Java)

A 2D platformer built in pure Java (Swing/AWT, no external game engine) using a component-oriented architecture. The player progresses through levels, collects coins and potions, avoids traps, and fights enemies (orcs).

## Table of Contents
- [Features](#features)
- [Project Architecture](#project-architecture)
- [Repository Structure](#repository-structure)
- [Requirements](#requirements)
- [Build & Run](#build--run)
- [Controls](#controls)
- [Testing](#testing)
- [Technologies](#technologies)

## Features
- Multiple game states: menu and gameplay (`Menu`, `Playing`), pause, death screen, level-complete screen.
- Component-based entity system (`Component`, `MovementComponent`, `PhysicsComponent`, `AnimationComponent`, `RenderComponent`, `InputComponent`).
- Factories for creating game objects and entities (`EntityFactory`, `DefaultEntityFactory`, `GameObjectFactory`).
- Managers for levels, enemies, and objects (`LevelManager`, `EnemyManager`, `ObjectManager`).
- An orc enemy (`Orc`) with its own behavior (`Enemy`).
- Interactive game items: coins (`Coin`), potions (`Potion`), traps (`Trap`).
- Custom exception handling (`ResourceLoadException`, `ResourceNotFoundException`, `ResourceReadException`, `LevelLoadException`).
- Logging via SLF4J + Logback.
- A unit test suite using JUnit 5 + Mockito.

## Project Architecture
The project follows a separation-of-concerns approach:

- **main** — entry point, main window, and game loop (`MainClass`, `Game`, `GameWindow`, `GamePanel`).
- **gamestates** — manages game states (menu / gameplay) and transitions between them.
- **entities** — the player, enemies, and base entity classes.
- **components** — individual behavior aspects (movement, physics, animation, rendering, input) attached to entities.
- **objects** — interactive game objects (coins, potions, traps) and their managers/factories.
- **levels** — level loading and management.
- **factories** — entity factories that create entities by type (`EntityType`).
- **Inputs** — keyboard and mouse input handling.
- **ui** — UI elements: buttons, pause/level-complete/game-over overlays.
- **utilz** — helper classes: resource loading (`LoadSave`), sprite atlases, constants, and other utilities.
- **exeptions** — custom exceptions for resource- and level-loading errors.

## Repository Structure
```
2d-Platformer-Game-Java-main/
├── src/
│   ├── main/            # entry point, window, game loop, resources (res/)
│   ├── gamestates/       # game states (Menu, Playing)
│   ├── entities/         # Player, Enemy, Orc, Entity
│   ├── components/       # entity components
│   ├── objects/          # Coin, Potion, Trap and their managers
│   ├── levels/           # Level, LevelManager
│   ├── factories/        # entity factories
│   ├── Inputs/            # keyboard/mouse input handling
│   ├── ui/                # buttons and overlays
│   ├── utilz/             # utilities, constants, resource loading
│   └── exeptions/         # custom exceptions
├── Test/                  # unit tests (mirrors the src/ structure)
├── pom.xml                 # Maven build configuration
└── README.md
```

## Requirements
- **Java 23** (`maven.compiler.source`/`target` = 23)
- **Maven** 3.6+

## Build & Run

### Using Maven
Build the project and produce an executable "fat" jar (with all dependencies bundled, via `maven-shade-plugin`):
```bash
mvn clean package
```

Run the game:
```bash
java -jar target/ReturnToMoria-1.0-SNAPSHOT.jar
```

### Using an IDE (IntelliJ IDEA)
1. Open the project as a Maven project (`pom.xml`).
2. Wait for the dependencies to download.
3. Run the `main.MainClass` class.

## Controls
| Key | Action |
|---|---|
| `A` | Move left |
| `D` | Move right |
| `Space` | Jump |
| `Esc` | Pause / unpause |

The menu and overlays (pause, game over, level complete) are controlled with the mouse.

## Testing
The project includes a unit test suite (JUnit 5 + Mockito) in the `Test/` folder, mirroring the `src/` package structure. Run the tests with:
```bash
mvn test
```

## Technologies
- Java 23, Swing/AWT (rendering and the game window)
- Maven (build, dependencies, fat-jar via `maven-shade-plugin`)
- SLF4J + Logback (logging)
- JUnit 5 + Mockito (testing)
