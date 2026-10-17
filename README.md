# Deep Sea Explorer 

![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JUnit 5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![OOP](https://img.shields.io/badge/Architecture-Object--Oriented-b52e31?style=for-the-badge)
![FSM](https://img.shields.io/badge/Pattern-Finite_State_Machine-007396?style=for-the-badge)
![Serialization](https://img.shields.io/badge/Data-Serialization-8A2BE2?style=for-the-badge)

Deep Sea Explorer is an object-oriented Java application that simulates oceanic exploration. Players manage a mother ship and a fleet of submersible drones to navigate through different depth zones, collect resources, avoid hazards, and solve puzzles in the ocean depths.

## Core Features
*   **Fleet Management:** Control a main vessel and manage a fleet of drones. Drones require periodic maintenance, fuel management, and integrity monitoring.
*   **Depth Zones:**
    *   **Surface:** The starting point for deployment and fleet management (repairs and refueling via the Workshop/Oficina).
    *   **Trench (Fosso):** A vertical descent zone where drones must navigate carefully.
    *   **Abyss (Fundo):** The ocean floor, containing valuable minerals, ancient artifacts, and dangerous sea monsters.
*   **Dynamic Hazards:** The ocean is populated with obstacles such as marine animals, water currents, rocks, and monsters, each with unique behaviors and damage mechanics (e.g., Rock collisions calculate damage using prime numbers).
*   **Resource Gathering:** Drones can collect minerals and unique artifacts during their dives, which are later unloaded at the mother ship.
*   **State Machine Architecture:** The game flow is governed by a robust Finite State Machine (FSM), ensuring strict rules for transitions between the Surface, Trench, Abyss, Workshop, and Puzzle states.
*   **Defense Mode:** An alternative difficulty mode that introduces predetermined hazards and reveals the entire abyss layout for strategic planning.

## Technical Stack & Concepts
*   **Language:** Java
*   **Architecture:** Object-Oriented Design (OOD), State Pattern (FSM)
*   **Testing:** JUnit 5 (Comprehensive unit testing for state transitions, resource management, and collision math).
*   **Data Persistence:** Serialization for saving and loading game states.

## Game States (FSM)
1.  **Superficie (Surface):** Deploy drones, recall the fleet, or enter the workshop.
2.  **Oficina (Workshop):** Repair damaged drones and refuel the fleet.
3.  **Fosso (Trench):** Navigate downwards to reach the ocean floor or ascend back to the surface. No repairs or refueling allowed.
4.  **Fundo (Abyss):** Explore the seabed to collect resources and discover artifacts while avoiding monsters.
5.  **Puzzle:** Activated upon finding an artifact. A grid-based mini-game with a limited number of moves. Esgoting moves forces a retreat to the Trench.
6.  **Fim de Jogo (Game Over):** Reached when all drones are lost or fuel is completely depleted. Locks all actions.

## Testing
The project includes extensive JUnit tests covering:
*   State machine transitions (e.g., ensuring drones cannot jump directly from the Surface to the Abyss).
*   Drone fuel consumption and cargo capacity limits.
*   Mathematical logic for damage calculation.
*   Save/Load data integrity.
