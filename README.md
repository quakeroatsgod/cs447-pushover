# cs447-pushover
A Java Pac-Man style game about pushing snowballs made with the Slick2D game engine.

## Description
The game has five states: StartUp, Playing, Win, Freeze, and GameOver. The game starts in StartUp
and displays a splash screen. The game then moves to Playing after receiving user input. The user
is able to control a green character that can move around in 4 directions. The player may not walk into
walls or trees. The player is able to push around a snow ball one tile at a time, at the cost of not
being able to move briefly.

The goal of the game is to destroy all red enemies with the snowball by pushing the snowball on top of them.
The enemies will attempt to walk towards the player (using A* path finding). If an enemy walks onto the
same tile as the player, the player loses a life. If the player loses all three lives, the game ends.
If all enemies are destroyed, the game advances to the next level.

## Controls
- W = move up
- S = move down
- A = move left
- D = move right

## Cheat codes
- Z = turn OFF enemy movement
- X = turn ON enemy movement
- C = turn ON enemy path highlighting
- V = turn OFF enemy path highlighting
- B = activate speed power-up
- N = activate freeze power-up
- 1 = start level 1
- 2 = start level 2
- 3 = win current level
- 4 = lose current level

## Low Bar Items
### - [x] = COMPLETE
- [x] Player Movement with WASD
- [x] Snowball Interaction and Usage
- [x] Deep Snow Tiles slow down entities
- [x] Enemies and pathfinding AI
- [x] Win/Lose conditions
- [x] Speed power-up
- [x] Freeze power-up
- [x] Life Tracker
- [x] Grid-based, maze-like map design


## Attributions
- Snowball, wall, blank tile, and deep snow textures taken from https://opengameart.org
- Character sprites licensed under CC BY-SA 3.0 taken from https://route1rodent.itch.io/16x16-rpg-character-sprite-sheet 
- Tree sprite taken from https://danaida.itch.io/free-pixel-trees-32x32
- Start, win, and game over screens are created by me.
- Ice-cube, freeze power-up and speed power-up sprites are made by me.

## License
Pushover is licensed under MIT. See the LICENSE file for a full description.