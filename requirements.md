# Requirements Document

## Introduction

This document specifies the requirements for a Stick War Legacy-inspired real-time strategy game built with Java 11 and LibGDX. The game features territory-based conquest mechanics where players build units, gather resources, and expand their territory across a 20-sector map. The project follows an MVC architecture pattern and is designed for a 2-week development sprint by a 4-person team.

## Glossary

- **GameMain**: The main application class extending LibGDX's Game class, serving as the entry point
- **Tile**: A discrete sector of the game map with ownership, terrain type, and accessibility properties
- **GameMap**: The world container holding 20 tiles representing the battlefield
- **TerritoryManager**: System responsible for calculating and updating tile ownership based on unit positions
- **Entity**: Abstract base class for all game objects with position, health, and ownership
- **Unit**: Abstract class extending Entity representing mobile combat/worker units
- **Building**: Abstract class extending Entity representing stationary structures
- **ResourceManager**: System managing the player's resources using Map<String, Integer>
- **UnitFactory**: Factory class for creating unit instances
- **GameHUD**: Heads-up display showing resources, territory control, and notifications
- **Scene2D**: LibGDX's UI framework for creating interactive interface elements
- **Frontline**: The furthest tile index controlled by a player based on unit positions

## Requirements

### Requirement 1: Project Structure and Core Architecture

**User Story:** As a developer, I want a well-organized LibGDX project skeleton with MVC architecture, so that the team can work on separate components without conflicts.

#### Acceptance Criteria

1. WHEN the project is initialized THEN the System SHALL create a Gradle-based LibGDX project targeting Java 11 with desktop module only
2. WHEN the project structure is generated THEN the System SHALL organize code into packages: core, entities, entities.buildings, resources, world, factories, screens, ui, and utils
3. WHEN the GameMain class is instantiated THEN the System SHALL extend LibGDX's Game class and initialize the MainMenuScreen
4. WHEN GameState is queried THEN the System SHALL return one of: MENU, PLAYING, VICTORY, or DEFEAT

### Requirement 2: Game Map and Tile System

**User Story:** As a player, I want a procedurally generated map with different terrain types, so that each game session offers varied strategic challenges.

#### Acceptance Criteria

1. WHEN a new game starts THEN the GameMap SHALL generate exactly 20 tiles indexed 0-19
2. WHEN tiles are generated THEN the System SHALL place the player base at Tile 0 and enemy base at Tile 19
3. WHEN tiles are generated THEN the System SHALL place GoldMine buildings at Tiles 5 and 14
4. WHEN terrain is procedurally generated THEN the System SHALL assign approximately 70% GRASS, 20% MOUNTAIN, and 10% WATER tiles using java.util.Random
5. WHEN a Tile has type MOUNTAIN THEN the Tile SHALL have a defenseBonus value of 2
6. WHEN a Tile has type WATER THEN the Tile SHALL have isAccessible set to false
7. WHEN a Tile is created THEN the Tile SHALL store: index, TileType, owner, isAccessible, and defenseBonus properties

### Requirement 3: Territory Expansion System

**User Story:** As a player, I want to expand my territory by advancing units across the map, so that I can conquer enemy land and win the game.

#### Acceptance Criteria

1. WHEN TerritoryManager.updateFrontline() is called THEN the System SHALL iterate through all player units and find the maximum tile index occupied
2. WHEN the frontline is calculated THEN the System SHALL set Tile.owner to the player for all tiles with index less than or equal to the frontline index
3. WHEN territory ownership changes THEN the GameHUD SHALL display the updated frontline percentage
4. WHEN a player controls Tile 19 (enemy base) THEN the GameState SHALL transition to VICTORY
5. WHEN the enemy controls Tile 0 (player base) THEN the GameState SHALL transition to DEFEAT

### Requirement 4: Entity System

**User Story:** As a developer, I want a hierarchical entity system with common properties, so that units and buildings share consistent behavior.

#### Acceptance Criteria

1. WHEN an Entity is created THEN the Entity SHALL have: id, x position, hp, maxHp, and owner properties
2. WHEN Entity.takeDamage(int dmg) is called THEN the Entity SHALL reduce hp by the damage amount
3. WHEN an Entity's hp reaches zero or below THEN the System SHALL mark the Entity for removal
4. WHEN a Unit is created THEN the Unit SHALL extend Entity and add: attackPower, moveSpeed, and UnitState properties
5. WHEN a Building is created THEN the Building SHALL extend Entity and include a Map<String, Integer> cost property

### Requirement 5: Unit Types

**User Story:** As a player, I want different unit types with unique abilities, so that I can employ varied tactical strategies.

#### Acceptance Criteria

1. WHEN UnitFactory.createUnit("Miner", owner) is called THEN the Factory SHALL return a Miner unit configured for resource gathering
2. WHEN UnitFactory.createUnit("Swordwrath", owner) is called THEN the Factory SHALL return a Swordwrath melee combat unit
3. WHEN UnitFactory.createUnit("Archidonis", owner) is called THEN the Factory SHALL return an Archidonis ranged combat unit
4. WHEN a Unit.move(float delta) is called THEN the Unit SHALL update its x position based on moveSpeed and delta time
5. WHEN a Unit.attack(Unit target) is called THEN the Unit SHALL deal damage to the target based on attackPower

### Requirement 6: Building Types

**User Story:** As a player, I want different building types for base defense and resource generation, so that I can establish a functioning economy.

#### Acceptance Criteria

1. WHEN a Base building is created THEN the Base SHALL be positioned at Tile 0 (player) or Tile 19 (enemy) and store goldStored property
2. WHEN a GoldMine building is created THEN the GoldMine SHALL be positioned at Tile 5 or 14 and track remainingGold property
3. WHEN a Tower building is created THEN the Tower SHALL have an attackRange property for defensive capabilities
4. WHEN a Building is instantiated THEN the Building SHALL define its construction cost in the cost Map

### Requirement 7: Resource Management

**User Story:** As a player, I want to gather and spend resources, so that I can build units and structures.

#### Acceptance Criteria

1. WHEN ResourceManager is initialized THEN the ResourceManager SHALL create a Map<String, Integer> with keys: "Or", "Bois", "Pierre", "Nourriture"
2. WHEN ResourceManager.addResource(String key, int amount) is called THEN the ResourceManager SHALL increase the specified resource by the amount
3. WHEN ResourceManager.getResource(String key) is called THEN the ResourceManager SHALL return the current amount of that resource
4. WHEN a player starts a new game THEN the ResourceManager SHALL initialize with 100 "Or" and 0 for other resources

### Requirement 8: Combat System

**User Story:** As a player, I want units to engage in combat with damage variance, so that battles have tactical depth and unpredictability.

#### Acceptance Criteria

1. WHEN combat occurs THEN the CombatSystem SHALL use java.util.Random to calculate damage variance
2. WHEN a melee unit attacks THEN the Unit SHALL deal damage only to adjacent targets
3. WHEN a ranged unit attacks THEN the Unit SHALL deal damage to targets within its attack range
4. WHEN a unit's target is destroyed THEN the Unit SHALL seek the next nearest enemy

### Requirement 9: User Interface - Main Menu

**User Story:** As a player, I want a main menu with navigation options, so that I can start games and access settings.

#### Acceptance Criteria

1. WHEN MainMenuScreen is displayed THEN the Screen SHALL show a "New Game" TextButton using Scene2D
2. WHEN MainMenuScreen is displayed THEN the Screen SHALL show an "Options" TextButton using Scene2D
3. WHEN MainMenuScreen is displayed THEN the Screen SHALL show a "Quit" TextButton using Scene2D
4. WHEN the "New Game" button is clicked THEN the System SHALL transition to GameScreen with a new GameMap
5. WHEN the "Quit" button is clicked THEN the System SHALL exit the application

### Requirement 10: User Interface - Game HUD

**User Story:** As a player, I want to see my resources and territory status during gameplay, so that I can make informed strategic decisions.

#### Acceptance Criteria

1. WHEN GameScreen is active THEN the GameHUD SHALL display player resources at the top-left of the screen
2. WHEN GameScreen is active THEN the GameHUD SHALL display frontline percentage at the top-center of the screen
3. WHEN GameScreen is active THEN the GameHUD SHALL display a notifications panel at the bottom of the screen
4. WHEN a game event occurs THEN the System SHALL add a notification string to the events ArrayList<String>
5. WHEN territory ownership changes THEN the GameHUD SHALL update the frontline percentage display

### Requirement 11: Game Screen Rendering

**User Story:** As a player, I want to see the game map with units and buildings rendered, so that I can observe and interact with the game world.

#### Acceptance Criteria

1. WHEN GameScreen renders THEN the Screen SHALL display all 20 tiles with visual distinction by TileType
2. WHEN GameScreen renders THEN the Screen SHALL display tile ownership through color tinting
3. WHEN GameScreen renders THEN the Screen SHALL display all units at their current positions
4. WHEN GameScreen renders THEN the Screen SHALL display all buildings at their tile positions

### Requirement 12: Options Screen

**User Story:** As a player, I want to access game options, so that I can adjust settings like volume.

#### Acceptance Criteria

1. WHEN OptionsScreen is displayed THEN the Screen SHALL show a "Volume" Label using Scene2D
2. WHEN OptionsScreen is displayed THEN the Screen SHALL show a volume Slider control stub
3. WHEN the back action is triggered THEN the System SHALL return to MainMenuScreen

### Requirement 13: Constants and Configuration

**User Story:** As a developer, I want centralized constants, so that game values are consistent and easily modifiable.

#### Acceptance Criteria

1. WHEN Constants class is accessed THEN the Class SHALL provide RESOURCE_KEYS array containing "Or", "Bois", "Pierre", "Nourriture"
2. WHEN tile types are needed THEN the TileType enum SHALL provide GRASS, MOUNTAIN, and WATER values
