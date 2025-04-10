# 🚦 Traffic Simulator Game

A JavaFX-based traffic control simulation game developed as a final project for the **CSE1242 Computer Programming II** course at Marmara University.

## 👥 Authors

- Berat Metehan Çakmak  
- Eray Hoşavcıoğlu 

---

## 🎮 Project Description

This project simulates traffic flow in a city using JavaFX. Players are responsible for controlling traffic lights to manage the movement of cars, prevent collisions, and ensure that a certain number of vehicles reach their destination.

Players interact with:
- Dynamic maps loaded from external text files
- Real-time GUI traffic control system
- Cars that move along predefined paths and respond to traffic lights
- Win/Lose conditions based on successful navigation and crashes

---

## 🔧 Implementation Overview

### 🧱 Object-Oriented Design:
- `MapElement` (abstract class) – Base class for all visible map elements  
- `Building`, `RoadTile`, `TrafficLight`, `Car` – Visual game objects rendered with JavaFX  
- `Game` – Manages logic, car spawning, updates, collisions  
- `GameUI` – Handles rendering and user interaction  
- `Path` – Defines movement for cars  
- `LevelLoader` – Loads map elements from external files  
- `Main` – Entry point for the application  

### 🔄 Game Mechanics:
- Cars are created periodically and follow paths
- Traffic lights can be toggled by the player (green/red)
- Cars react to light states and other cars
- Collisions are detected and handled
- Game ends with a **win** (X cars succeed) or **loss** (Y crashes occur)

---

## ✅ Completed Features

- 🚗 Dynamic car movement and traffic light interaction  
- 🗺️ Level loading system with map, roads, and buildings  
- 🎨 Fully functional JavaFX GUI  
- 🔁 Singleton pattern used in game controller

---


## 🧪 Test Cases

- Game starts correctly from menu and loads selected level  
- Traffic lights toggle and affect car behavior in real-time  
- Cars navigate paths and avoid collisions at intersections  
- Win/Loss messages are triggered and displayed properly  
- Map elements render correctly from input files  

---

## 📚 References

- JavaFX API – Oracle Docs  
- Stack Overflow  
- Java Programming by Y. Daniel Liang   
- Eclipse IDE Docs  

---

## 🖥️ Technologies

- Java 17  
- JavaFX  
- Eclipse IDE  
- OOP, GUI Programming, File Parsing  
