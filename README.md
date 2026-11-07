# 🐾 Cat Hero | Modular 2D Arcade Engine

> A clean, decoupled, and reusable 2D arcade engine built in Java using Swing, featuring an 80s retrowave rescue game ("Cat Hero").

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)

---

## 🏗️ Architecture

The project follows a strict **Engine + Game pattern**, separating core window/loop logic, asset management, state machine handling, and UI utilities from specific game rules.

```text
org.example/
│
├── engine/                   # Reusable Arcade Core
│   ├── GameEngine.java       # Main game loop, FPS control, and state machine
│   ├── GamePanel.java        # Interface for pluggable game modules
│   └── UIUtils.java          # Reusable arcade widgets (HUD, flashing text)
│
├── AssetManager.java         # Centralized texture and image loader
├── SoundManager.java         # Audio handler for BGM and sound effects
├── ScoreManager.java         # Generic score/high-score manager with lambda predicates
├── GameConstants.java        # Centralized configuration (dimensions, physics, rules)
├── Player.java               # Player entity mechanics & bounds
├── FallingObject.java        # Dynamic game entities / obstacles / collectibles
├── Particle.java             # Particle effect system for impacts
└── ModernGame.java           # Specific game implementation ("Cat Hero")
```

---

## ✨ Features

* **Decoupled Core Loop (`GameEngine`)**: Ensures a stable game loop, fixed timing, window handling, and centralized input dispatching.
* **State Machine (`GameState`)**: Handles transitions between `MENU`, `PLAYING`, `GAME_OVER`, and `VICTORY` states, including input cooldown protection and state-based audio switching.
* **Functional Victory Conditions**: `ScoreManager` uses functional `Predicate` rules, keeping high-score persistence generic while allowing games to define unique win criteria.
* **Reusable Arcade UI (`UIUtils`)**: Static helper utilities to render styled text overlay elements and blinking press-any-key indicators.
* **Retrowave Style & Dynamic Difficulty**: Features retro visuals, dynamic entity spawning speeds based on score, and particle explosion systems upon impact.

---

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or higher.

### Running the Project

1. Clone the repository:
   `git clone [https://github.com/YOUR-USERNAME/cat-hero-arcade-engine.git](https://github.com/YOUR-USERNAME/cat-hero-arcade-engine.git)`
2. Open the project in your preferred Java IDE.
3. Run the `main` method in `ModernGame.java`.

---

## 🕹️ Controls

* **Left / Right Arrows** (or **A / D**): Move horizontal direction.
* **Any Key**: Start game from menu or return to menu from end states (after cooldown).
* **ESC**: Exit application.

---

## 📜 License

Distributed under the **MIT License**. See `LICENSE` for more information.