# POO Stickman:  Complete Game Analysis

## 📋 Game Overview

**POO Stickman** is a **Real-Time Strategy (RTS) Tower Defense Battle Game** featuring stick figure armies fighting for territorial control.  It's a faithful recreation of the classic "Stick War:  Legacy" game, built entirely in Java using the libGDX framework.

**Game Resolution**: 1280×720 @ 60 FPS  
**Platform**: Desktop (Windows, Mac, Linux)  
**Language**: 100% Java with Gradle build system

---

## 🎮 Core Gameplay Loop

### Game Flow
```
START → MAIN MENU → SELECT "NEW GAME"
  ↓
BATTLE SCREEN (Real-time gameplay)
  ↓
WIN/LOSE CONDITION MET
  ↓
VICTORY/DEFEAT SCREEN → RETURN TO MENU → LOOP
```

### Victory/Defeat Conditions
- **VICTORY**: Destroy the enemy's Command Center (reduce HP to 0 or below)
- **DEFEAT**: Your Command Center is destroyed
- **REWARD**: Win 10 Gems + 5 Upgrade Points per victory
- These rewards accumulate throughout the game session

---

## 🗺️ Game Map & Territory System

### Map Layout
- **Dimensions**: 64 tiles wide × 8 tiles tall (horizontal battleground)
- **Aspect Ratio**:  Designed for left-vs-right confrontation
- **Two Bases**: 
  - Player base on the LEFT (position:  50, 360)
  - Enemy base on the RIGHT (position: 1230, 360)

### Territory Management
The game features a **dynamic territory control system**:
- **TerritoryManager** tracks territory ownership based on unit positions
- Territory is determined by the **frontline** - the furthest position of your units
- **All tiles up to your frontline are controlled by you**
- Territory is displayed as a **Territory Progress Bar** in the HUD: 
  - **Blue section**: Player's controlled territory
  - **Red section**: Enemy's controlled territory
  - Shows the current battle position at a glance

### Tile System
Each tile has:
- **Position** (x, y coordinates)
- **Owner** (which player controls it)
- **Type** (grass, stone, etc.)
- Updates dynamically as units advance

---

## 💰 Economic System:  Gold Resource

### Gold Management
- **Primary Currency**: Gold
- **Starting Amount**: 200 gold per player
- **Resource Manager**:  Tracks player finances with a simple HashMap

### Gold Operations
| Operation | Effect |
|-----------|--------|
| `addGold(amount)` | Increase balance |
| `spendGold(amount)` | Deduct from balance (returns true/false) |
| `getGold()` | Check current balance |
| `canAfford(cost)` | Verify if you have enough gold |
| `reset()` | Return to 200 gold (for testing) |

### How Gold is Spent
Gold is exclusively used to **spawn/recruit units** from your Barracks:
- Each unit type has a fixed gold cost
- When you spawn a unit: 
  1. Check if you have sufficient gold
  2. Deduct the cost from your balance
  3. Create the unit at the spawn point
  3. Unit appears on the battlefield

### Income Generation
- **Miners** can enter MINING state to passively generate gold over time
- This creates strategic choices:  short-term military power vs. long-term economic advantage

---

## 👥 Unit System:  4 Distinct Unit Types

### 1. **MINER** 💰
- **Role**: Economic generator / Light support
- **HP**: Low-moderate
- **Attack Power**: Low
- **Movement Speed**:  Moderate
- **Cost**:  Cheapest unit
- **Special States**: Can enter MINING mode to gather resources
- **Strategy**: Economic play - use for passive income generation
- **Best For**: Building a strong economy for future units

### 2. **SWORDWRATH** ⚔️
- **Role**: Fast melee attacker / Frontline assault
- **HP**:  Moderate
- **Attack Power**: Medium
- **Movement Speed**: Fast
- **Cost**: Mid-range
- **Attack Range**: 50 units
- **Attack Cooldown**: 1.0 second
- **Strategy**: Aggressive early-game tactics
- **Best For**: Quick offensive pushes, exploiting weaknesses

### 3. **ARCHIDONIS** 🏹
- **Role**: Ranged damage dealer / Artillery support
- **HP**: Lower (fragile)
- **Attack Power**:  High
- **Movement Speed**: Moderate
- **Cost**: Mid-range
- **Attack Range**:  50+ units (ranged advantage)
- **Strategy**: Positional play - keep safe while dealing damage
- **Best For**: Supporting frontline units, area damage

### 4. **SPEARTON** 🛡️
- **Role**: Heavy tank / Defensive specialist
- **HP**: 150 (highest in game)
- **Attack Power**: 20 (moderate-high)
- **Movement Speed**:  70 (slowest unit)
- **Cost**: 500 gold (most expensive)
- **Special Ability**: Shield Defense
  - Reduces incoming damage by 30% (multiplier: 0.7x)
  - Can activate blocking mode for enhanced protection
- **Strategy**: Tanking damage, protecting weaker units
- **Best For**:  Holding ground, protecting miners and archers

### Unit Control System
**User Control Modifiers** - When players directly control a unit:
- **Damage Multiplier**: 2.0x (double damage)
- **Speed Multiplier**: 1.2x (20% faster movement)

This incentivizes active player involvement rather than pure automation. 

### Unit States (Life Cycle)
```
IDLE → MOVING → ATTACKING → RETURNING
  ↓                              ↓
  └──────────────────────────────┘
                ↓
              MINING (Miners only)
                ↓
              DEAD (Removed from game)
```

---

## 🏗️ Buildings

### Barracks (Unit Production)
- **HP**: 500
- **Location**: Each player has one at their base
- **Function**: Spawns units when commanded
- **Cost**: Units cost gold to spawn
- **Behavior**: Waits for player commands (not auto-producing)

### Command Center (Base / Objective)
- **HP**: Unknown (but critical)
- **Location**: Player base (left) and Enemy base (right)
- **Function**: Primary objective building
- **Loss Condition**: If destroyed, that player loses immediately
- **Victory Condition**: Destroying enemy's Command Center wins the game

---

## ⚔️ Combat System

### Damage Calculation
The combat system includes **random variance**:
- **Base Damage**: Unit's attack power
- **Random Variance**: +0 to +5 additional damage per hit
- **Formula**: `Damage = baseAttackPower + random(0-5)`

### Damage Modifiers
**Spearton Shield Defense**:  Reduces all incoming damage
- All damage to Spearton is multiplied by 0.7x (30% reduction)
- Example: 20 damage → 14 damage (approximately)

### Combat Range
- **Default Attack Range**: 50 units (distance-based)
- **Range Check**: `isInRange()` calculates Euclidean distance
- **Only attacks within range**: Units automatically attack enemies in range

### Attack Mechanics
- **Attack Cooldown**: 1.0 second per unit between attacks
- **Attack Processing**: `processAttack()` handles full combat cycle: 
  1. Check if attacker is in range
  2. Calculate damage with variance
  3. Apply target-specific modifiers (e.g., Spearton armor)
  4. Apply damage to target entity
  5. Check if target dies (HP ≤ 0)

### Health System
- **Max HP**: Each entity has maximum health
- **Current HP**: Decreases with damage
- **Death**:  When HP ≤ 0:
  - Entity is marked for removal
  - `onDeath()` callback triggered (can be overridden)
  - Unit removed from battlefield

---

## 🎨 User Interface (HUD)

### In-Game Display
The **GameHUD** shows real-time information: 

1. **Gold Counter** (Top-Left)
   - Displays current gold balance
   - Updated in real-time as units spawn

2. **Territory Progress Bar** (Top-Center)
   - Visual representation of map control
   - **Blue section**: Player territory
   - **Red section**: Enemy territory
   - Shows which side is winning territorially

3. **Notifications Panel** (Bottom)
   - Placeholder for game messages
   - Future:  Unit spawned, enemy spotted, etc.

### Rendering
- **2D Orthographic Camera**: 1280×720 view
- **Shape Renderer**: Draws map tiles and territory bars
- **Sprite Batch**: Renders text and UI elements
- **60 FPS Cap**: Ensures smooth gameplay

---

## 🎬 Game Screens & State Management

### Screen Types

#### 1. **Main Menu Screen**
- **Buttons**: "New Game", "Options", "Quit"
- **Scene2D UI**: Built with libGDX Scene2D framework
- **Transitions**: 
  - New Game → GameScreen
  - Options → OptionsScreen
  - Quit → Exit application

#### 2. **Game Screen** (Battlefield)
- The main gameplay environment
- Shows map, units, buildings, HUD
- Handles real-time updates and combat
- Checks victory/defeat conditions each frame

#### 3. **Options Screen**
- **Volume Slider**: Audio control (currently stub)
- **Back Button**: Returns to Main Menu
- Framework for future settings

#### 4. **Victory Screen**
- Triggered when enemy Command Center is destroyed
- Shows rewards:  10 Gems + 5 Upgrade Points
- Accumulates total progression across sessions

#### 5. **Defeat Screen**
- Triggered when player's Command Center is destroyed
- Game over state
- Returns to Main Menu

### Game States (Enum)
```java
MENU      // Main menu
PLAYING   // Active gameplay
VICTORY   // Player won
DEFEAT    // Player lost
PAUSED    // Game paused (future)
```

---

## 🔧 Game Architecture & Systems

### Core Components

#### **GameMain** (Entry Point)
- Extends libGDX's `Game` class
- Manages screen transitions
- Tracks global game state
- Accumulates rewards (Gems & Upgrade Points)
- Methods: 
  - `startGame()` → Begin gameplay
  - `showVictory(gemsEarned, upgradePointsEarned)` → End game victorious
  - `showDefeat()` → End game defeated
  - `returnToMenu()` → Reset state

#### **GameScreen** (Battlefield)
Manages the active battle with:
- **Players**: Human (Player 1) vs AI (Player 2)
- **Game Map**: 64×8 tile battlefield
- **Territory Manager**: Tracks territory ownership
- **Resource Managers**: Gold for both players
- **Buildings**: Both Command Centers and Barracks
- **HUD**: Real-time information display
- **Camera**: Orthographic view of the battlefield

#### **Player Class**
- **ID**: Unique identifier (1 or 2)
- **Name**: Display name ("Player" or "Enemy")
- **isHuman**: Boolean flag (true = human, false = AI)
- Represents both human and AI-controlled sides

#### **Entity System** (Object-Oriented Hierarchy)
```
Entity (Abstract Base)
├── Unit (Abstract)
│   ├── Miner
│   ├── Swordwrath
│   ├── Archidonis
│   └── Spearton
└── Building
    ├── CommandCenter
    └── Barracks
```

All entities have:
- Position (x, y)
- Health (hp, maxHp)
- Owner (which player)
- Removal state (marked for deletion)

#### **UnitFactory** (Factory Pattern)
Creates units by type string:
```java
factory.createUnit("Miner", owner)
factory.createUnit("Swordwrath", owner)
factory.createUnit("Archidonis", owner)
factory.createUnit("Spearton", owner)
```

Also tracks unit costs for validation.

#### **CombatSystem** (Combat Engine)
- Calculates damage with randomness
- Applies unit modifiers (User Control:  2x damage)
- Applies target modifiers (Spearton armor:  0.7x damage)
- Checks attack range
- Processes complete attack cycles
- Includes random seed for testing

#### **TerritoryManager** (Map Control)
- Calculates frontline position (furthest unit advance)
- Updates tile ownership based on unit positions
- Provides frontline percentage for HUD display
- Tracks territorial advantages

#### **ResourceManager** (Economy)
- Manages gold for each player
- Validates gold availability
- Processes unit spawning costs
- Tracks balance changes

---

## 🧪 Code Quality & Testing

The codebase includes comprehensive **property-based tests**:

### Test Categories
1. **GameState Tests**:  State transition validation
2. **ResourceManager Tests**: Gold economy round-trip testing (100 repeated tests each)
3. **Barracks Tests**: Unit spawning with cost validation
4. **CombatSystem Tests**: Damage calculation with variance

### Property Testing Examples
- "For any amount, addGold followed by getGold returns the correct value"
- "For any spend amount ≤ balance, spendGold succeeds and reduces balance exactly"
- "For any spend amount > balance, spendGold fails and balance unchanged"
- "Random damage variance always falls within [base, base+5]"

This ensures robust game mechanics. 

---

## 🎯 Gameplay Strategy Layer

### Economic Strategy
- **Miners vs.  Combat Units**: Do you invest in economy or military?
- **Gold Management**: Limited starting gold (200) forces tough choices
- **Cost-Benefit**:  Expensive Spearton (500) vs.  cheap Miner (unknown cost)

### Military Strategy
- **Unit Composition**: Mix of fast Swordwrath, ranged Archidonis, tanky Spearton
- **Frontline Control**: Push forward to gain territory
- **Defensive Play**: Use Spearton to hold ground
- **Economic Pressure**: Send Miners to generate passive income

### Tactical Play
- **Positioning**: Keep Archidonis safe behind Speartons
- **Wave Attacks**: Coordinate unit movements for synchronized attacks
- **User Control Bonus**: Actively controlling units gives 2x damage advantage
- **Range Management**: Attack from outside enemy attack range

---

## 📊 Game Statistics & Progression

### Session Rewards
- **Gems**: Currency for progression (10 per victory)
- **Upgrade Points**:  Enhance units (5 per victory)
- **Accumulation**: Tracked across entire session

### Unit Statistics
| Unit | HP | Attack | Speed | Cost | Range |
|------|----|---------|---------|---------|----|
| Miner | Low | Low | Mod | Low | 50 |
| Swordwrath | Mod | Med | Fast | Mid | 50 |
| Archidonis | Low | High | Mod | Mid | 50 |
| Spearton | 150 | 20 | 70 | 500 | 50 |

---

## 🚀 Technical Stack

### Framework & Libraries
- **libGDX**: Cross-platform game framework
- **Scene2D**: UI toolkit for menus and HUD
- **ShapeRenderer**: 2D shape drawing (tiles, bars)
- **OrthographicCamera**: 2D camera system

### Project Structure
```
poo-stickman/
├── core/                 # Game logic (all gameplay code)
│   ├── com. stickwar.legacy. core/        # Game state & players
│   ├── com.stickwar. legacy.screens/     # UI screens
│   ├── com.stickwar.legacy.entities/    # Units & buildings
│   ├── com.stickwar. legacy.systems/     # Combat & factories
│   ├── com.stickwar. legacy.resources/   # Gold management
│   ├── com.stickwar. legacy.world/       # Map & territory
│   ├── com.stickwar. legacy.ui/          # HUD rendering
│   └── tests/                           # Unit & property tests
└── desktop/              # Desktop launcher & configuration
```

### Build System
- **Gradle**: Multi-module build (core + desktop)
- **Java 8+**: Required JDK version
- **LWJGL3**: Desktop windowing (for libGDX)

---

## 🎓 Design Patterns Used

1. **Factory Pattern**: UnitFactory creates units dynamically
2. **Singleton-like**:  ResourceManager per player
3. **Observer Pattern**: Screen transitions via GameMain
4. **Strategy Pattern**: Different unit behaviors
5. **Template Method**: Entity base class with subclasses
6. **State Machine**: GameState enum for game flow

---

## 💡 Key Features

✅ **Real-time Strategy Gameplay** - Simultaneous player and AI decisions  
✅ **Territory Control System** - Visual map control representation  
✅ **Economic Simulation** - Gold-based unit economy  
✅ **Diverse Units** - 4 unique unit types with different roles  
✅ **Combat Variance** - Random damage for unpredictability  
✅ **Defensive Mechanics** - Spearton shield reduces damage  
✅ **User Control Bonus** - Active play rewards (2x damage)  
✅ **Progression System** - Gems and Upgrade Points accumulation  
✅ **Win/Loss Conditions** - Clear objectives (destroy enemy base)  
✅ **Comprehensive Testing** - Property-based test suite  

---

## 🎮 How to Play

1. **Launch Game** → Main Menu appears
2. **Click "New Game"** → Battle screen loads
3. **Earn Gold** → Starts with 200 gold
4. **Build Army** → Click to spawn units from Barracks (costs gold)
5. **Attack** → Units auto-attack enemies in range
6. **Manage Resources** → Balance military and economic units
7. **Push Forward** → Advance frontline to control territory
8. **Win Condition** → Destroy enemy Command Center
9. **Rewards** → Gain gems and upgrade points
10. **Repeat** → Play again from Main Menu

